package cn.pmm.dockertest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.pmm.dockertest.dao.BookRepository;
import cn.pmm.dockertest.entity.Book;

/**
 * @author panmiaomiao
 *
 * @date 2018年4月8日
 */
@Controller
@RequestMapping("/book")
public class BookController {
	private BookRepository bookRepository;

	@Autowired
	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@RequestMapping(value = "/{reader}", method = RequestMethod.GET)
	public String getByReader(@PathVariable("reader") String reader, Model model) {
		List<Book> readingList = bookRepository.findByReader(reader);
		if (readingList != null) {
			model.addAttribute("books", readingList);
		}
		return "bookList";
	}

	@RequestMapping(value = "/{reader}", method = RequestMethod.POST)
	public String addToBookList(@PathVariable("reader") String reader, Book book) {
		book.setReader(reader);
		bookRepository.save(book);
		return "redirect:/book/{reader}";
	}

}
