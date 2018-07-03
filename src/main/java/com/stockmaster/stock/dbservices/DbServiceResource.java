package com.stockmaster.stock.dbservices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResource {

	private QuotesRepository quotesRepository;

	public DbServiceResource(QuotesRepository quotesRepository) {
		this.quotesRepository = quotesRepository;
	}
	
	

	@GetMapping("/{username}")
	public List<String> getQuotes(@PathVariable("username") final String username) {
		return getByUserName(username);
	}

	@PostMapping("/delete")
	public boolean delete(@PathVariable("username") final String username) {
		List<Quote> quotes = quotesRepository.findByUserName(username);
		quotes.stream()
			.forEach(quote -> {
				quotesRepository.delete(quote);
			});
		return true;
	}
	
	@PostMapping("/add")
	public List<String> add(@RequestBody final Quotes quotes) {
		addQuote(quotes);
		return getByUserName(quotes.getUsername());
	}

	public List<String> getByUserName(String username) {
		return quotesRepository.findByUserName(username).stream().map(Quote::getQuote).collect(Collectors.toList());
	}

	public void addQuote(Quotes quotes) {
		quotes.getQuotes().stream()
		.map(quote -> new Quote(quotes.getUsername(),quote))
		.forEach(quote -> {
			quotesRepository.save(quote);
		});
	}

}
