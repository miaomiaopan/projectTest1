package cn.pmm.dockertest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pmm.dockertest.entity.Book;

/**
 * @author panmiaomiao
 *
 * @date 2018年4月8日
 */
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByReader(String reader);
}
