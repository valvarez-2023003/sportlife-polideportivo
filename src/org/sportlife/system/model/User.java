package org.sportlife.system.model;

/**
 * Modelo que representa un usuario del sistema.
 * Puede ser Gerente, Administrador, Recepcionista o User (cliente).
 *
 * @author Cristofer Ramos
 */
public class User {

    private String idUser;
    private String name;
    private String lastname;
    private String email;
    private String user;
    private String password;
    private String role;
    private String phone;

    public User() {
    }

    public User(String idUser, String name, String lastname, String email,
                String user, String password, String role) {
        this.idUser = idUser;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.user = user;
        this.password = password;
        this.role = role;
    }

    // Getters y setters
    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}