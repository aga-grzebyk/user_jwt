# user_jwt
Spring Security - JWT Token, MySql, java8


System posiada role: ADMIN, TEACHER, USER; 
Encje: User;

Basic Spring Boot Security / .authenticationProvider() / .jwtToken()

logowanie
http://www.localhost/login 
--> metoda POST  
--> w body przesyłamy JSON i pobieramy token do autoryzacji:

{
  "username":"student1@java.dev",
  "password":"student123"
}

{
  "username":"teacher@java.dev",
  "password":"teacher123"
}

{
  "username":"admin@java.dev",
  "password":"admin123"
}

("/test").authenticated()
("/teacher/reports").hasAnyRole("TEACHER, ADMIN")
("/admin/user", "user/**").hasRole("ADMIN)

user wyszukiwany po id i name;
