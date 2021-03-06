package edu.itk.project.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import edu.itk.project.security.dto.ErrorDto;
import edu.itk.project.security.dto.ProjectRequest;
import edu.itk.project.security.dto.PatchProjectRequest;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ClientServerController {

	@Autowired
	private WebClient webClient;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/")
	public Map<String, String> setSecurityContextHolder(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient) {		
		String user = oauth2AuthorizedClient.getPrincipalName();
		
		String query = "SELECT authorities.authority FROM users, authorities WHERE users.username = authorities.username AND users.username = ?;";
		String role = (String) jdbcTemplate.queryForObject(query, new Object[] { user }, String.class);
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(role));

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);  
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Map<String, String> json = new LinkedHashMap<>();
		json.put("user", user);
		json.put("role", role);
		
		return json;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/projects")
	public Object getAllProjects(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient, @RequestParam(name = "search", required = false)Optional<String> searchValue) {
		return this.webClient
				.get()
				.uri("http://10.5.0.7:8090/projects", uri -> uri.queryParamIfPresent("search", searchValue).build())
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/projects/{id}")
	public Object getProjectById(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient, @PathVariable("id")int id) {
		return this.webClient
				.get()
				.uri("http://10.5.0.7:8090/projects/{id}", id)
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER') || hasRole('ROLE_USER')")
	@GetMapping("/projects/my_projects")
	public Object getProjectsByUsername(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient) {
		String username = oauth2AuthorizedClient.getPrincipalName();
		
		return this.webClient
				.get()
				.uri("http://10.5.0.7:8090/projects/my_projects/{username}", username)
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/projects")
	public Object postProject(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient, @RequestBody ProjectRequest request) {
		return this.webClient
				.post()
				.uri("http://10.5.0.7:8090/projects")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), ProjectRequest.class)
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
	@PatchMapping("/projects/{id}")
	public Object patchProject(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient, @PathVariable("id") int id, @RequestBody PatchProjectRequest request) {
		return this.webClient
				.patch()
				.uri("http://10.5.0.7:8090/projects/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), PatchProjectRequest.class)
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MANAGER')")
	@PatchMapping("/projects/close/{id}")
	public Object closeProject(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient, @PathVariable("id") int id) {
		return this.webClient
				.patch()
				.uri("http://10.5.0.7:8090/projects/close/{id}", id)
				.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
					if(response.statusCode().isError()) {
						return response.bodyToMono(ErrorDto.class);
					} else {
						return response.bodyToMono(Object.class);
					}
				}).block();
	}
	
}
