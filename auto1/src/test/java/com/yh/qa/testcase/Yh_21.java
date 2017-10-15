package com.yh.qa.testcase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.yh.qa.basecase.BaseTestCase;
import com.yh.qa.entity.BoCiInfo;
import com.yh.qa.entity.GJOrderStatus;
import com.yh.qa.entity.OrderDetail;
import com.yh.qa.entity.UserInfo;
import com.yh.qa.service.LoginService;
import com.yh.qa.service.OrderService;
import com.yh.qa.util.ValidateUtil;

import io.restassured.path.json.JsonPath;

/**
 * @author panmiaomiao
 *
 * @date 2017年10月10日
 */
@SpringBootTest
public class Yh_21 extends BaseTestCase {
	@Autowired
	private LoginService loginService;

	@Autowired
	private OrderService orderService;

	@Test
	public void testHttpPost() throws Exception {
		String query = "";
		String body = "";
		JsonPath jsonPath = null;
		String accessTokenSH = "";
		String accessTokenGJ = "";
		UserInfo userInfo = null;
		List<BoCiInfo> boCiInfos = null;
		List<String> boCiIds = new ArrayList<String>();
		// 余额
		int balance;
		// 积分
		Double credit;
		// 订单总数
		int num;
		// 待评价订单数量
		int toComment;
		// 待配送订单数量
		int toDelivery;
		// 订单状态
		int status;

		// 测试数据 门店和商品
		String storeId = "9485";
		String lat = "31.335144";
		String lng = "121.284953";
		String sku = "B-336006";
		Double price = 7.50d;
		Double quantity = 1d;
		Double pricetota = 0d;

		// 下单用户
		String phonenum = "18729555529";
		String securitycode = "601933";

		// 拣货员
		String jhy = "9485100";
		String password = "123456a";

		// 配送员
		String psy = "9485201";
		String psyId = "50012079";

		try {
			// 设置case名称
			testcase.setTestName("Bravo 集波履单，非自配送，配送单");

			// case开始执行
			// 登录永辉生活app
			query = "?platform=ios";
			body = "{\"phonenum\": \"" + phonenum + "\", \"securitycode\": \"" + securitycode + "\"}";
			userInfo = loginService.loginSHAndGetUserInfo(query, body, 0);
			accessTokenSH = userInfo.getAccess_token();
			balance = userInfo.getBalance();
			credit = userInfo.getCredit();
			num = userInfo.getNum();
			toComment = userInfo.getToComment();
			toDelivery = userInfo.getToDelivery();

			// 生成bravo绿标店当日达配送订单
			query = "?channel=qa3&deviceid=000000000000000&platform=Android&v=4.2.2.1&access_token=" + accessTokenSH
					+ "&timestamp=" + System.currentTimeMillis();
			body = "{\"balancepayoption\":1,\"device_info\":\"864854034674759\",\"freedeliveryoption\":1,\"paypasswordtype\":0,\"pickself\":0,\"pointpayoption\":0,\"pricetotal\":"
					+ pricetota + ",\"products\":[{\"id\":\"" + sku + "\",\"isbulkitem\":0,\"num\":" + quantity * 100
					+ ",\"pattern\":\"t\"}],\"recvinfo\":{\"address\":{\"area\":\"汉泰中泰融合餐厅(嘉定大融城店)\",\"city\":\"上海\",\"cityid\":\"1\",\"detail\":\"111\"},\"alias\":\"家\",\"foodsupport\":0,\"id\":\"32354\",\"isSearch\":false,\"isdefault\":1,\"itemType\":0,\"location\":{\"lat\":\""
					+ lat + "\",\"lng\":\"" + lng
					+ "\"},\"name\":\"小潘\",\"nextdaydeliver\":0,\"phone\":\"18729555529\",\"scope\":0},\"sellerid\":3,\"storeid\":\""
					+ storeId + "\",\"texpecttime\":{\"date\":"
					+ (System.currentTimeMillis() / 86400000 * 86400000 - 28800000)
					+ ",\"timeslots\":[{\"immediatedesc\":\"60分钟达\",\"slottype\":\"immediate\"}]},\"totalpayment\":0,\"uid\":\"836248696765411806\"}";
			jsonPath = orderService.confirm(query, body, 0);
			String orderId = jsonPath.getString("orderid");

			// 重新登录永辉生活APP刷新用户信息
			query = "?platform=ios";
			body = "{\"phonenum\": \"18729555529\", \"securitycode\": \"601933\"}";
			userInfo = loginService.loginSHAndGetUserInfo(query, body, 0);
			accessTokenSH = userInfo.getAccess_token();

			Assert.isTrue(userInfo.getBalance() + (price * quantity + 6) * 100 == balance, "下单支付后用户余额减少数额错误");
			Assert.isTrue(userInfo.getNum() - 1 == num, "下单支付后订单总数没有加1");
			Assert.isTrue(userInfo.getToDelivery() - 1 == toDelivery, "下单后待配送订单总数没有加1");

			// 使用拥有bravo拣货员角色的账号登录管家APP进行拣货
			query = "?platform=android";
			body = "{\"pwd\": \"" + password + "\", \"username\": \"" + jhy + "\"}";
			jsonPath = loginService.loginGJ(query, body, 0);
			accessTokenGJ = jsonPath.getString("token");

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.PENDING.getIndex(), "下单完成后订单状态不是待确认状态");

			Thread.sleep(30000);

			// 获取待接单的波次号信息
			query = "?page=0&filter=0&timestamp=" + System.currentTimeMillis()
					+ "&platform=Android&channel=qa3&access_token=" + accessTokenGJ;
			jsonPath = orderService.waitingPack(query, 0);
			boCiInfos = jsonPath.getList("orders", BoCiInfo.class);
			for (BoCiInfo entity : boCiInfos) {
				boCiIds.add(String.valueOf(entity.getPickwaveid()));
			}

			// 接单
			for (String waveId : boCiIds) {
				query = "/" + waveId + "?" + "waveId=" + waveId + "&access_token=" + accessTokenGJ + "&timestamp="
						+ System.currentTimeMillis() + "&platform=ios&channel=qa3";
				body = "{\"waveId\": \"" + waveId + "\"}";
				jsonPath = orderService.startPack(query, body, 0);
			}

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.START_PACK.getIndex(), "接单后的状态不是开始拣货状态");

			// 拣货完成
			for (String waveId : boCiIds) {
				query = "/" + waveId + "?" + "waveId=" + waveId + "&access_token=" + accessTokenGJ + "&timestamp="
						+ System.currentTimeMillis() + "&platform=ios&channel=qa3";
				body = "{\"waveId\": \"" + waveId + "\"}";
				jsonPath = orderService.completePack(query, body, 0);
			}

			Thread.sleep(3000);

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.WAITING_ASSIGN.getIndex(), "拣货完成后的状态不是等待分配的状态");

			// 拣货员派单
			query = "?platform=ios&access_token=" + accessTokenGJ + "&timestamp=" + System.currentTimeMillis();
			body = "{\"orderid\":\"" + orderId + "\",\"assignto\":" + psyId + ",\"action\":5}";
			jsonPath = orderService.orderAction(query, body, 0);

			Thread.sleep(5000);

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.WAITING_TAKE.getIndex(), "派单后的状态不是等待接单的状态");

			// 配送员登录管家APP进行配送
			query = "?platform=android";
			body = "{\"pwd\": \"" + password + "\", \"username\": \"" + psy + "\"}";
			jsonPath = loginService.loginGJ(query, body, 0);
			accessTokenGJ = jsonPath.getString("token");

			// 接单
			query = "?platform=ios&access_token=" + accessTokenGJ + "&timestamp=" + System.currentTimeMillis();
			body = "{\"orderid\": \"" + orderId + "\", \"action\": \"1\"}";
			jsonPath = orderService.orderAction(query, body, 0);

			Thread.sleep(5000);

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.READY_TO_PICKUP.getIndex(), "接单后的状态不是待提的状态");

			// 提货
			query = "?platform=ios&access_token=" + accessTokenGJ + "&timestamp=" + System.currentTimeMillis();
			body = "{\"orderid\": \"" + orderId + "\", \"action\": \"2\"}";
			jsonPath = orderService.orderAction(query, body, 0);

			Thread.sleep(5000);

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.PICKUP.getIndex(), "提货后的状态不是提货状态");

			// 核销
			query = "?platform=ios&access_token=" + accessTokenGJ + "&timestamp=" + System.currentTimeMillis();
			body = "{\"orderid\": \"" + orderId + "\", \"action\": \"3\"}";
			jsonPath = orderService.orderAction(query, body, 0);

			Thread.sleep(5000);

			// 调用管家的订单详情接口获取订单状态
			query = "?platform=Android&orderid=" + orderId + "&access_token=" + accessTokenGJ + "&id="
					+ userInfo.getUId();
			jsonPath = orderService.detailGj(query, 0);
			status = jsonPath.getInt("status");
			Assert.isTrue(status == GJOrderStatus.COMPLETE.getIndex(), "核销后的状态不是已完成的状态");

			// 重新登录永辉生活APP刷新用户信息
			query = "?platform=ios";
			body = "{\"phonenum\": \"" + phonenum + "\", \"securitycode\": \"" + securitycode + "\"}";
			userInfo = loginService.loginSHAndGetUserInfo(query, body, 0);
			accessTokenSH = userInfo.getAccess_token();

			Assert.isTrue(userInfo.getToComment() - 1 == toComment, "核销后待评价订单总数没有加1");

			// 积分校验
			List<OrderDetail> goodsArr = new ArrayList<OrderDetail>();
			goodsArr.add(new OrderDetail(quantity, price));
			Double tempCredit = ValidateUtil.calculateCredit2(goodsArr);
			System.out.println(tempCredit + "**" + credit);
			Assert.isTrue(userInfo.getCredit() - tempCredit == credit, "核销后用户积分增加不正确");

			// 登出永辉生活app
			query = "?platform=Android&access_token=" + accessTokenSH;
			jsonPath = loginService.loginOutSH(query, 0);

		} catch (Exception e) {
			testcase.setStatus("FAIL");
			testcase.setDescription(e.getMessage());
			throw e;
		} finally {
			// TODO 刪除测试数据
		}

	}
}
