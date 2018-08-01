package com.cgpicaporte.springboot.webflux.app.contorllers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.cgpicaporte.springboot.webflux.app.models.dao.ProductoDao;
import com.cgpicaporte.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {

	@Autowired
	private ProductoDao dao;

	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping({"/listar","/"})
	public String listar(Model model) {
		
		Flux<Producto> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
		
		
		model.addAttribute("productos", productos);//cuando pasamos los datos hace el subscribe. Thimeleaf es el observador que se suscribe a la plantilla
		model.addAttribute("titulo", "LISTADO DE PRODUCTOS");
		return "listar";
	}
	
	@GetMapping("/listar-datadriver")
	public String listarDataDriver(Model model) {
		
		Flux<Producto> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).delayElements(Duration.ofSeconds(1));
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
				
		//model.addAttribute("productos", productos);//cuando pasamos los datos hace el subscribe. Thimeleaf es el observador que se suscribe a la plantilla
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));//escriba cada segundo 2 productos
		model.addAttribute("titulo", "LISTADO DE PRODUCTOS");
		return "listar";
	}
	
	@GetMapping("/listar-full")
	public String listarFull(Model model) {
		
		Flux<Producto> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);
		//En el navegador vemos que tarda en recibir la inforación 4.69 segundos
		
		
		
		model.addAttribute("productos", productos);//cuando pasamos los datos hace el subscribe. Thimeleaf es el observador que se suscribe a la plantilla
		model.addAttribute("titulo", "LISTADO DE PRODUCTOS");
		return "listar";
	}
	
	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {
		
		Flux<Producto> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);
		//En el navegador vemos que tarda en recibir la inforación 4.69 segundos
		
		
		
		model.addAttribute("productos", productos);//cuando pasamos los datos hace el subscribe. Thimeleaf es el observador que se suscribe a la plantilla
		model.addAttribute("titulo", "LISTADO DE PRODUCTOS");
		return "listar-chunked";
	}
}
