package com.projecty.projectyweb.project.role;

import com.projecty.projectyweb.ProjectyWebApplication;
import com.projecty.projectyweb.project.Project;
import com.projecty.projectyweb.project.ProjectRepository;
import com.projecty.projectyweb.user.User;
import com.projecty.projectyweb.user.UserRepository;
import com.projecty.projectyweb.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectyWebApplication.class)
public class ProjectRoleServiceTests {
    @Autowired
    private ProjectRoleService projectRoleService;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRoleRepository projectRoleRepository;


    @MockBean
    private UserService userService;

    private User user;
    private User user1;

    @Before
    public void init() {
        user = new User();
        user.setUsername("user");
        userRepository.save(user);

        user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        Mockito.when(userService.getCurrentUser())
                .thenReturn(user);
    }

    @Test
    public void whenAddTeamRolesByUsernames_shouldReturnTeamWithTeamRoles() {
        Project project = new Project();
        project.setName("Sample project");
        project = projectRepository.save(project);
        List<String> usernames = new ArrayList<>();
        usernames.add(user.getUsername());
        usernames.add(user.getUsername());
        usernames.add(user1.getUsername());
        usernames.add(user1.getUsername());
        projectRoleService.addRolesToProjectByUsernames(project, usernames, null);

        Optional<Project> optionalProject = projectRepository.findById(project.getId());
        if (optionalProject.isPresent()) {
            assertThat(projectRoleRepository.findRoleByUserAndProject(user, optionalProject.get()), is(notNullValue()));
            assertThat(projectRoleRepository.findRoleByUserAndProject(user, optionalProject.get()), is(notNullValue()));
        } else {
            assert false;
        }
    }
}
