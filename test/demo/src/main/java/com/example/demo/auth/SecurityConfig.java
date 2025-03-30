/*
package com.example.demo.auth;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;

@Configuration

public class SecurityConfig {

    */
/*public static String[] PUBLIC_ENDPOINTS = {"/api/auth/login", "/api/users", "/error"};

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Mặc định nếu như chúng ta đã impl lại SecurityFilterChain thì spring sẽ bác bo cơ chế xác thực mặc định
        // Nếu như ta khong chỉ định cơ chế xác thực thì sẽ luon bị chặn dù cho là public enpoint
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }*//*


 @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                        request.requestMatchers("/api/public").permitAll()
                                .requestMatchers("/api/read").hasAnyAuthority("READ", "ALL_PERMISSIONS")
                                .requestMatchers("/api/write").hasAnyAuthority("WRITE", "ALL_PERMISSIONS")
                                //.requestMatchers("/api/readwrite").hasAnyAuthority("ROLE_ADMIN", "READ", "WRITE") // nếu sử dụng như thể này thì chắc chắn chỉ cần một trong các quyền là có thể access
                                // cần sử dụng cách khác để ngăn chặn trường hợp này
                                //.requestMatchers("/api/readwrite").access("hasAuthority('READ') and hasAuthority('WRITE')") cách này không con được sử dụng nữa
                                .requestMatchers("/api/readwrite").access(allOf(
                                        hasAnyAuthority("READ", "ALL_PERMISSIONS"),
                                        hasAnyAuthority("WRITE", "ALL_PERMISSIONS")
                                ))
                                .requestMatchers("/h2-console/**", "/h2-console").hasAnyAuthority("ALL_PERMISSIONS")
                                //.hasRole("ADMIN")// chỉ cần đặt ADMIN spring security sẽ tự thêm tiền tố ROLE_ vào để hiểu không cần phải đặt ROLE_ADMIN vào đó
                                .anyRequest().hasAnyAuthority("ALL_PERMISSIONS", "ROLE_USER")
        // tất cả các dang asRole hay RequestMatchers điều cần trả về dạng AuthorizeHttpRequestsConfigurer
        ).httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults()); // phai bật httpBasic để test bằng Basic Auth trong post man vì spring 6.0 không cho phép bật tự động nữa
                //.formLogin(Customizer.withDefaults())   ;// thiết lập mặc định trang login
        // việc thiết lập nay nếu có lỗi thì sẽ ngay lập tức quăng lại trang login nêu không có hợp lệ
        // tắt nó thì sẽ quăn lại lỗi 403 - forbidden (Không có quyền)
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = //User.withDefaultPasswordEncoder() // có thể sử dụng dạng mã có mã hóa
        User.withUsername("user")
                .password("{noop}12345")// do là không sử dụng mã hóa mặc định nên phải dụng noop là để mật khau đã nhìn thấy
                .authorities("READ", "WRITE", "ROLE_USER") // chỉ cần ở trên có bất kì đường link có authority nào READ là sẽ thành công
                .build();
        // User là đối tượng được implement lại UserDetails và các method build này được xây dựng trên các field có sẵn mặc dinh của spring security

        UserDetails admin = //User.withDefaultPasswordEncoder()
                User.withUsername("admin")
                .password("{bcrypt}$2y$10$gaWCgA9L.d1WnZbx6rLPTeoW990RDAxKgog89NF7DxGgf06mEucRS") //12345 với việc này thì sẽ cung cấp {bcrypt} để spring tu biết mã hóa theo cách nào
                //.roles("ADMIN") // spring sẽ tự thêm tiền to ROLE_ vào để hiểu la user nây có quyền ROLE_ADMIN
                .authorities("ALL_PERMISSIONS") // với method authorities này thì mặc định sẽ không thêm ROLE vào mà ta phải tự thêm thủ công
                .build();

        return new InMemoryUserDetailsManager(user, admin);
        // InMemoryUserDetailsManager là class ược xây dựng từ UserDetailsManager với mục đích lưu User vào RAM thay vì DB
    } // vì nó kế thừa từ UserDetailsManager và UserDetailsManager được kế thừa từ UserDetailsService nên method trả về như vậy


}
*/
