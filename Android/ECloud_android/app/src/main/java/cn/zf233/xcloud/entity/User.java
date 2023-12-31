package cn.zf233.xcloud.entity;

/**
 * Created by zf233 on 11/28/20
 */
public class User {

    private Integer id;
    private String username;
    private String nickname;
    private String password;
    private Integer role;
    private Integer capacity;;
    private Integer level;
    private Integer growthValue;

    public User() {
    }

    public User(Integer id, String username, String nickname, String password, Integer role, Integer useCapacity, Integer level, Integer growthValue) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.capacity = useCapacity;
        this.level = level;
        this.growthValue = growthValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(Integer growthValue) {
        this.growthValue = growthValue;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
