package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class StrumienieMain {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(StrumienieMain.class, args);
		ProductRepo productRepo = context.getBean(ProductRepo.class);
		OrderRepo orderRepo = context.getBean(OrderRepo.class);
		ClientRepo clientRepo = context.getBean(ClientRepo.class);
		
		List<Product> result1 = productRepo.findAll()
				  .stream()
				  .filter(p -> p.getCategory().equalsIgnoreCase("Smartphones"))
				  .filter(p -> p.getDescription().contains("waterproof"))
				  .filter(p -> p.getDescription().contains("120Hz"))
				  .filter(p -> p.getDescription().contains("128GB"))
				  .collect(Collectors.toList());	
		
	    Optional<Product> result2 = productRepo.findAll()
	            .stream()
	            .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
	            .filter(p -> p.getPrice() < 100)
	            .min(Comparator.comparing(Product::getPrice));
		
	    Double result3 = productRepo.findAll()
	    		.stream()
	    		.filter(p -> p.getCategory().equalsIgnoreCase("Clothes"))
	    		.mapToDouble(p -> p.getPrice())
	    		.average().getAsDouble();
	     
	    Map<String, List<Client>> result4 = clientRepo.findAll()
	    		.stream()
	    		.filter(c -> c.getEmail().contains("@gmail.com"))
	            .collect(
	            	Collectors.groupingBy(Client::getLastName)	         		
	                );
	    
	    Map<String, Optional<Product>> result5 = productRepo.findAll()
	            .stream()
	            .collect(
	                Collectors.groupingBy(
	                    Product::getCategory,
	                    Collectors.maxBy(Comparator.comparing(Product::getPrice))));
	    
	    List<Order> result6 = orderRepo.findAll()
	            .stream()
	            .filter(o -> o.getProducts().stream().anyMatch(p -> p.getCategory().equalsIgnoreCase("Books")))
	            .filter(o -> o.getProducts().stream().anyMatch(p -> p.getPrice() < 80))
	            .collect(Collectors.toList());  
	    
	    List<Product> result7 = orderRepo.findAll()
	    	    .stream()
	    	    .filter(o -> o.getDeliveryDate().isEqual(LocalDate.of(2021, 3, 15)))
	    	    .filter(o -> o.getTypeDelivery().equalsIgnoreCase("inPost"))
	    	    .flatMap(o -> o.getProducts().stream())
	    	    .distinct()
	    	    .collect(Collectors.toList());
	    
	    Map<String, List<Double>> result8 = productRepo.findAll()
	            .stream()
	            .filter(p -> p.getCategory().equalsIgnoreCase("Garden"))
	            .collect(
	                Collectors.groupingBy(
	                    Product::getName,
	                    Collectors.mapping(product -> product.getPrice(), Collectors.toList()))
	                );
	    	    
	    List<Order> result9 = orderRepo.findAll()
	            .stream()
	            .sorted(Comparator.comparing(Order::getShippedDate).reversed())
	            .limit(20)
	            .collect(Collectors.toList());    
	    
	    Map<Long, String>  result10 = orderRepo.findAll()
	            .stream()
	            .collect(
	                Collectors.toMap(
	                    order -> order.getId(),
	                    order -> order.getClient().getEmail()
	                    )
	                );
  	    
		List<Product> result11 = orderRepo.findAll()
				.stream()
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2022, 3, 0)) >= 0)
				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2022, 4, 15)) <= 0)
				.filter(o -> o.getClient().getEmail().equals("example@gmai.com"))
				.flatMap(o -> o.getProducts().stream()).distinct().collect(Collectors.toList());
		
	    Double result12 = orderRepo.findAll()
	    	    .stream()
				.filter(o -> o.getShippedDate().compareTo(LocalDate.of(2022, 3, 0)) >= 0)
				.filter(o -> o.getShippedDate().compareTo(LocalDate.of(2022, 4, 15)) <= 0)
	    	    .flatMap(o -> o.getProducts().stream())
	    	    .mapToDouble(p -> p.getPrice())
	    	    .sum();		
	    
	    Map<Order, Double> result13 = orderRepo.findAll()
	            .stream()
	            .collect(
	              Collectors.toMap(
	            	  Function.identity(), 
	                  order -> order.getProducts().stream()
	                        .mapToDouble(p -> p.getPrice()).sum()
	                ) 
	              );      
	}
}
