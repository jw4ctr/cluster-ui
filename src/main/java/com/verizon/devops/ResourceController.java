package com.verizon.devops;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.verizon.devops.jpa.Priv;
import com.verizon.devops.jpa.PrivRepository;
import com.verizon.devops.jpa.Project;
import com.verizon.devops.jpa.ProjectRepository;
import com.verizon.devops.jpa.Role;
import com.verizon.devops.jpa.RoleRepository;
import com.verizon.devops.jpa.Service;
import com.verizon.devops.jpa.ServiceRepository;
import com.verizon.devops.jpa.ServiceRole;
import com.verizon.devops.jpa.ServiceRoleRepository;
import com.verizon.devops.jpa.Stage;
import com.verizon.devops.jpa.StageRepository;
import com.verizon.devops.jpa.User;
import com.verizon.devops.jpa.UserRepository;

//TODO exception handling
//TODO logging
@RestController
@RequestMapping("/clusterui")
public class ResourceController {
	@Autowired 
	private UserRepository userRepository;
		
	@Autowired 
	private ProjectRepository projectRepository;
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ServiceRoleRepository serviceRoleRepository;
	
	@Autowired
	private PrivRepository privRepository;
	
	@Autowired 
	private StageRepository stageRepository;


	//TODO ????
	@GetMapping(path="/isauthenticated/{user}")
	public @ResponseBody Boolean isAuthenticated(@PathVariable("user") String user) {
		return true;
	}
	
	//TODO ????
	@GetMapping(path="/authenticate/{user}")
	public @ResponseBody Boolean authenticate(@PathVariable("user") String user) {		
		return true;
	}
	
	//TODO ????
	@PutMapping(path="/login/{user}")
	public @ResponseBody Boolean login(@PathVariable("user") String user) {
		return true;
	}
	
	//TODO ????
	@PutMapping(path="/logout/{user}")
	public @ResponseBody Boolean logout(@PathVariable("user") String user) {
		return true;
	}
	
	@GetMapping(path="/users")
	public @ResponseBody Iterable<User> listUsers() {
		return userRepository.findAll();
	}		
	
	@GetMapping(path="/users/{user}")
	public @ResponseBody User getUser (@PathVariable("user") String userName) {
		return userRepository.findByName(userName);
	}
	
	@PostMapping(path="/users")
	public @ResponseBody User addUser (@Valid @RequestBody User user) {
		//TODO: check unique and fail if not
		return userRepository.save(user);		
	}
		
	@PutMapping(path="/users/{user}")
	public @ResponseBody User updateUser (@PathVariable("user") String userName, @Valid @RequestBody User user) {
		User theUser = getUser(userName);
		user.setId(theUser.getId());		
		
		Set<ServiceRole> srMerged = new HashSet<ServiceRole>();
		for(ServiceRole sr : user.getRoles()) {
			boolean existing = false;
			for(ServiceRole theSr : theUser.getRoles()) {
				if (sr.equals(theSr)) {
					srMerged.add(theSr);
					existing = true;
					break;
				}
				
				if (sr.getTheService().equals(theSr.getTheService())) {
					sr.setTheService(theSr.getTheService());
				}
				
				if (sr.getTheService().getTheProject().equals(theSr.getTheService().getTheProject())) {
					sr.getTheService().setTheProject(theSr.getTheService().getTheProject());
				}
				
				if (sr.getTheEnv().equals(theSr.getTheEnv())) {
					sr.setTheEnv(theSr.getTheEnv());
				}
				
				if (sr.getTheRole().equals(theSr.getTheRole())) {
					sr.setTheRole(theSr.getTheRole());
				}
			}
			
			if (!existing) srMerged.add(sr);
		}
		user.setRoles(srMerged);
		
		Set<Project> projectsMerged = new HashSet<Project>();
		for(Project proj : user.getProjects()) {
			boolean existing = false;
			for (Project theProj : theUser.getProjects()) {
				if (proj.equals(theProj)) {
					projectsMerged.add(theProj);
					existing = true;
					break;
				}
			}
			if (!existing) projectsMerged.add(proj);
		}
		user.setProjects(projectsMerged);

		
		return userRepository.save(user);
	}
	
	@DeleteMapping(path="/users/{user}")
	public @ResponseBody ResponseEntity<?> deleteUser (@PathVariable("user") String userName) {
		userRepository.deleteByName(userName);
		return ResponseEntity.ok().build();
	}
		
	@PutMapping(path="/users/{user}/projects/{project}/add") 
	public @ResponseBody ResponseEntity<?> assignUserToProject (@PathVariable("user") String userName, 
			@PathVariable("project") String projectName) {
		
		User theUser = getUser(userName);
		Project theProject = this.getProject(projectName);
		theUser.getProjects().add(theProject);
		userRepository.save(theUser);
		
		return ResponseEntity.ok().build();
	}	
	
	@PutMapping(path="/users/{user}/projects/{project}/remove") 
	public @ResponseBody ResponseEntity<?>  removeUserFromProject (@PathVariable("user") String userName, @PathVariable("project") String projectName) {
		User theUser = getUser(userName);
		
		Project theProject = this.getProject(projectName);
		theUser.getProjects().remove(theProject);
		
		Set<ServiceRole> serviceRolesUpdated = new HashSet<ServiceRole>();		
		Set<ServiceRole> serviceRoles = theUser.getRoles();
		for(ServiceRole sr : serviceRoles) {
			if (!sr.getTheService().getTheProject().getName().equalsIgnoreCase(projectName)) {
				serviceRolesUpdated.add(sr);
			}
		}
		theUser.setRoles(serviceRolesUpdated);
		userRepository.save(theUser);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping(path="/users/{user}/services/{service}/{role}/{stage}/add") 
	public @ResponseBody ResponseEntity<?>  assignUserToService (@PathVariable("user") String userName, 
			@PathVariable("service") String serviceName, @PathVariable("role") String roleName, @PathVariable("stage") String stageName) {
		
		User theUser = getUser(userName);
		
		ServiceRole theServiceRole = serviceRoleRepository.findByServiceNameAndRoleNameAndStage(serviceName, roleName, stageName);
		if (theServiceRole == null) {
			Role theRole = getRole(roleName);
			Stage theStage = getStage(stageName);
			Service theService = getService(serviceName);
			theServiceRole = new ServiceRole();
			theServiceRole.setName(serviceName +"-"+roleName+"-"+stageName);
			theServiceRole.setTheEnv(theStage == null ? new Stage(stageName) : theStage);
			theServiceRole.setTheRole(theRole == null ? new Role(roleName, "") : theRole);
			theServiceRole.setTheService(theService == null ? new Service(serviceName) : theService);
		}
		theUser.getRoles().add(theServiceRole);
	
		userRepository.save(theUser);
		
		return ResponseEntity.ok().build();
	}	

	
	@PutMapping(path="/users/{user}/services/{service}/remove") 
	public @ResponseBody ResponseEntity<?> removeUserFromService (@PathVariable("user") String userName, @PathVariable("service") String serviceName) {
		User theUser = getUser(userName);
		
		List<ServiceRole> theServiceRoles = serviceRoleRepository.findByServiceName(serviceName);
		theUser.getRoles().removeAll(theServiceRoles);
		
		userRepository.save(theUser);
		
		return ResponseEntity.ok().build();	
	}
	
	@GetMapping(path="/users/{user}/projects")
	public @ResponseBody Iterable<Project> getProjectsForUser(@PathVariable("user") String userName) {		
		return userRepository.findByName(userName).getProjects();
	}
	
	@GetMapping(path="/users/{user}/services")
	public @ResponseBody Iterable<Service> getServicesForUser(@PathVariable("user") String userName) {
		// This returns a JSON or XML with the users
		Set<Service> services = new HashSet<Service>();
		Set<ServiceRole> serviceRoles = userRepository.findByName(userName).getRoles();
		for (ServiceRole serviceRole : serviceRoles) {
			services.add(serviceRole.getTheService());
		}
		return services;
	}		
	
	@GetMapping(path="/projects")
	public @ResponseBody Iterable<Project> listProjects () {
		return projectRepository.findAll();
	}
	
	@GetMapping(path="/projects/{project}/services")
	public @ResponseBody Iterable<Service> listServicesInProject (@PathVariable("project") String projectName) {
		return serviceRepository.findByProjectName(projectName);
	}
	
	@GetMapping(path="/projects/{project}")
	public @ResponseBody Project getProject (@PathVariable("project") String projectName) {
		return projectRepository.findByName(projectName);
	}
	
	@PostMapping(path="/projects")
	public @ResponseBody Project addProject (@Valid @RequestBody Project project) {
		return projectRepository.save(project);		
	}
		
	@PutMapping(path="/projects/{project}")
	public @ResponseBody Project updateProject (@PathVariable("project") String projectName, @Valid @RequestBody Project project) {
		project.setId(getProject(projectName).getId());
		return projectRepository.save(project);	
	}
	
	@DeleteMapping(path="/projects/{project}")
	public @ResponseBody ResponseEntity<?> deleteProject (@PathVariable("project") String projectName) {
		projectRepository.deleteByName(projectName);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path="/services")
	public @ResponseBody Iterable<Service> listServices () {
		return serviceRepository.findAll();
	}
	
	@GetMapping(path="/services/{service}")
	public @ResponseBody Service getService (@PathVariable("service") String serviceName) {
		return serviceRepository.findByName(serviceName);
	}
	
	@PostMapping(path="/services")
	public @ResponseBody Service addService (@Valid @RequestBody Service service) {
		return serviceRepository.save(service);		
	}
		
	@PutMapping(path="/services/{service}")
	public @ResponseBody Service updateService (@PathVariable("service") String serviceName, @Valid @RequestBody Service service) {
		Service theService = getService(serviceName);
		service.setId(theService.getId());
		
		service.setTheProject(getProject(service.getTheProject().getName()));
		
		return serviceRepository.save(service);	
	}
	
	@DeleteMapping(path="/services/{service}")
	public @ResponseBody ResponseEntity<?> deleteService (@PathVariable("service") String serviceName) {
		serviceRepository.deleteByName(serviceName);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path="/roles")
	public @ResponseBody Iterable<Role> listRoles () {
		return roleRepository.findAll();
	}
	
	@GetMapping(path="/roles/{role}")
	public @ResponseBody Role getRole (@PathVariable("role") String roleName) {
		return roleRepository.findByName(roleName);
	}
	
	@PostMapping(path="/roles")
	public @ResponseBody Role addRole (@Valid @RequestBody Role role) {
		return roleRepository.save(role);		
	}
		
	@PutMapping(path="/roles/{role}")
	public @ResponseBody Role updateRole (@PathVariable("role") String roleName, @Valid @RequestBody Role role) {
		Role theRole = getRole(roleName);
		role.setId(theRole.getId());
		
		Set<Priv> mergedPrivs = new HashSet<Priv>();
		Set<Priv> privs = role.getPrivs();
		for(Priv priv : privs) {
			Priv privFound = getPriv(priv.getName());
			mergedPrivs.add(privFound == null ? priv : privFound);
		}
		role.setPrivs(mergedPrivs);
		
		return roleRepository.save(role);	
	}
	
	@DeleteMapping(path="/roles/{role}")
	public @ResponseBody ResponseEntity<?> deleteRole (@PathVariable("role") String roleName) {
		roleRepository.deleteByName(roleName);
		return ResponseEntity.ok().build();
	}	
	
	@GetMapping(path="/privs")
	public @ResponseBody Iterable<Priv> listPrivs () {
		return privRepository.findAll();
	}
	
	@GetMapping(path="/privs/{priv}")
	public @ResponseBody Priv getPriv (@PathVariable("priv") String privName) {
		return privRepository.findByName(privName);
	}
	
	@PostMapping(path="/privs")
	public @ResponseBody Priv addPriv (@Valid @RequestBody Priv priv) {
		return privRepository.save(priv);		
	}
		
	@PutMapping(path="/privs/{priv}")
	public @ResponseBody Priv updatePriv (@PathVariable("priv") String privName, @Valid @RequestBody Priv priv) {
		priv.setId(getPriv(privName).getId());
		return privRepository.save(priv);	
	}
	
	@DeleteMapping(path="/privs/{priv}")
	public @ResponseBody ResponseEntity<?> deletePriv (@PathVariable("priv") String privName) {
		privRepository.deleteByName(privName);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path="/stages")
	public @ResponseBody Iterable<Stage> listStages () {
		return stageRepository.findAll();
	}
	
	@GetMapping(path="/stages/{stage}")
	public @ResponseBody Stage getStage (@PathVariable("stage") String stageName) {
		return stageRepository.findByName(stageName);
	}
	
	@PostMapping(path="/stages")
	public @ResponseBody Stage addStage (@Valid @RequestBody Stage stage) {
		return stageRepository.save(stage);		
	}
		
	@PutMapping(path="/stages/{stage}")
	public @ResponseBody Stage updateStage (@PathVariable("stage") String stageName, @Valid @RequestBody Stage stage) {
		stage.setId(getStage(stageName).getId());
		return stageRepository.save(stage);	
	}
	
	@DeleteMapping(path="/stages/{stage}")
	public @ResponseBody ResponseEntity<?> deleteStage (@PathVariable("stage") String stageName) {
		stageRepository.deleteByName(stageName);
		return ResponseEntity.ok().build();
	}
	
}
