package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;

import java.net.URI;
import java.net.URISyntaxException;
//spring.cloud.bootstrap.enabled=true
@SpringBootApplication
@RestController
public class DemoApplication  {
	@Autowired
	VaultOperations vaultOperations;

	@RequestMapping("/")
	public String home() {

		VaultTemplate vaultTemplate = null;
		VaultEndpoint endpoint = null;
		try {
			endpoint = VaultEndpoint.from( new URI("http://localhost:8200"));
			System.out.println("=============1");
			System.out.println(endpoint);
			System.out.println("=============1");
			vaultTemplate = new VaultTemplate( endpoint,
					new TokenAuthentication("hvs.kY5OOmVaZfWsWNvXe785a9Za"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		Secrets secrets = new Secrets();
		secrets.username = "hello";
		secrets.password = "world";

		//vaultTemplate.write("secret/fakebank", secrets);
		System.out.println("=============2");
		System.out.println(vaultTemplate);
		System.out.println("=============2");

		//VaultResponseSupport<Secrets> response = vaultTemplate.read("secret/fakebank", Secrets.class);
		//VaultResponse response = vaultTemplate.read("/secret/fakebank/username");
		VaultKeyValueOperations versioned = vaultOperations.opsForKeyValue("secret",KeyValueBackend.KV_2);
		System.out.println("=============3");
		System.out.println(versioned);
		System.out.println("=============3");
		//versioned.put("myapplication/", secrets);



		VaultResponseSupport<Secrets> response = versioned.get("fakebank", Secrets.class);


		System.out.println("=============4");
		System.out.println(response);
		System.out.println("=============4");
		Secrets data = response.getData();
		System.out.println("=============5");
		System.out.println(data.password);
		System.out.println(data.username);
		System.out.println("=============5");
		//vaultTemplate.delete("secret/myapp");

		return "Hello World!";
	}



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


}
