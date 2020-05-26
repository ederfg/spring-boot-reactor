package com.bolsadeideas.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.app.models.Comentarios;
import com.bolsadeideas.springboot.reactor.app.models.Usuario;
import com.bolsadeideas.springboot.reactor.app.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("Haola mundo");

		// ejemploIterable(args);
		// ejemploFlatMap();
		// ejemploToString();
		//ejemploToCollectList();
		//ejemploUsuarioComentariosFlatMap();
		ejemploUsuarioComentariosZipWith();
	}
	
	public void ejemploUsuarioComentariosZipWithForma2() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));
		
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola como estás");
			comentarios.addComentario("Mañana vdoy a la playa!");
			comentarios.addComentario("Estoy tomando el curso de spring reactivo");
			return comentarios;
		});
		Mono<UsuarioComentarios> usuarioConComentarios = usuarioMono.zipWith(comentariosUsuarioMono).map(tuple->{
			Usuario u = tuple.getT1();
			Comentarios c = tuple.getT2();
			return new UsuarioComentarios(u, c);
		});
		usuarioConComentarios.subscribe(uc-> log.info(uc.toString()));
	}
	

	public void ejemploUsuarioComentariosZipWith() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));
		
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola como estás");
			comentarios.addComentario("Mañana vdoy a la playa!");
			comentarios.addComentario("Estoy tomando el curso de spring reactivo");
			return comentarios;
		});
		Mono<UsuarioComentarios> usuarioConComentarios = usuarioMono.zipWith(comentariosUsuarioMono,(usuario,comentariosUsuarios)->new UsuarioComentarios(usuario,comentariosUsuarios));
		usuarioConComentarios.subscribe(uc-> log.info(uc.toString()));
	}
	
	
	public void ejemploUsuarioComentariosFlatMap() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));
		
		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola como estás");
			comentarios.addComentario("Mañana vdoy a la playa!");
			comentarios.addComentario("Estoy tomando el curso de spring reactivo");
			return comentarios;
		});
		
		
		usuarioMono.flatMap( u-> comentariosUsuarioMono.map(c-> new UsuarioComentarios(u, c)))
		.subscribe(uc-> log.info(uc.toString()));
	}

	public void ejemploToCollectList(String... args) throws Exception {
		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Bernardo", " Guzman"));
		usuariosList.add(new Usuario("Pepe", "Flores"));
		usuariosList.add(new Usuario("Eder", "Flores"));
		usuariosList.add(new Usuario("Julia", "Guzman"));
		usuariosList.add(new Usuario("Maria", "Cabrera"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Williams"));

		Flux.fromIterable(usuariosList).collectList().subscribe(lista -> {
			lista.forEach(e -> log.info(e.toString()));
		});
	}

	public void ejemploToString(String... args) throws Exception {
		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Bernardo", " Guzman"));
		usuariosList.add(new Usuario("Pepe", "Flores"));
		usuariosList.add(new Usuario("Eder", "Flores"));
		usuariosList.add(new Usuario("Julia", "Guzman"));
		usuariosList.add(new Usuario("Maria", "Cabrera"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Williams"));

		Flux.fromIterable(usuariosList)
				.map(elemento -> elemento.getNombre().toUpperCase() + " " + elemento.getApellido().toUpperCase())
				.flatMap(usuario -> {
					if (usuario.contains("bruce".toUpperCase())) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				}).map(usuario -> usuario.toLowerCase()).subscribe(e -> log.info(e.toString()));
	}

	public void ejemploFlatMap(String... args) throws Exception {
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Bernardo Guzman");
		usuariosList.add("Pepe Flores");
		usuariosList.add("Eder Flores");
		usuariosList.add("Julia Guzman");
		usuariosList.add("Maria Cabrera");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Williams");

		Flux.fromIterable(usuariosList).map(
				elemento -> new Usuario(elemento.split(" ")[0].toUpperCase(), elemento.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.getNombre().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				}).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				}).subscribe(e -> log.info(e.toString()));
	}

	public void ejemploIterable(String... args) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("Haola mundo");

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Bernardo Guzman");
		usuariosList.add("Pepe Flores");
		usuariosList.add("Eder Flores");
		usuariosList.add("Julia Guzman");
		usuariosList.add("Maria Cabrera");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Williams");

		Flux<String> nombres = Flux.fromIterable(usuariosList);

		Flux<Usuario> usuarios = nombres
				.map(elemento -> new Usuario(elemento.split(" ")[0].toUpperCase(),
						elemento.split(" ")[1].toUpperCase()))
				.filter(f -> f.getNombre().contains("B")).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("El nombre no puede estár vacio");
					}
					System.out.println(usuario.toString());
				}).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				})

		;

		usuarios.subscribe(e -> log.info(e.toString()), err -> log.error(err.getMessage()),

				new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						log.info("Se completa con éxito el observable");
					}
				});
	}

}
