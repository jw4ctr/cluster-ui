package com.verizon.devops.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class UserRepositoryTest {
	@Autowired 
	private UserRepository userRepository;
	
	@Test
	public void testSave() {
		User tester1 = new User("tester1", "tester1@verizon.com");
		tester1 = userRepository.save(tester1);

		User tester2 = new User("tester2", "tester2@verizon.com");
		tester2 = userRepository.save(tester2);

		User result = userRepository.findByName("tester1");
		assertNotNull(result);
		assertTrue(result.getName().equalsIgnoreCase(tester1.getName()));
	}
	
	//TODO: add more test methods here
}
