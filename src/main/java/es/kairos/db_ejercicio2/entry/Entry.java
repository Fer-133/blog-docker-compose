package es.kairos.db_ejercicio2.entry;

import es.kairos.db_ejercicio2.comment.Comment;

import javax.persistence.*;
import java.util.List;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String nickname;
    private String title;
    private String intro;
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Entry() {
    }

    public Entry(String name, String nickName, String title, String intro, String content) {
        this.name = name;
        this.nickname = nickName;
        this.title = title;
        this.intro = intro;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
