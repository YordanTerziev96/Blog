package softuniBlog.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Length(min = 1)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    @Length(min = 1)
    private String content;

    @ManyToOne(optional = false)
    private User author;

    public Article() {

    }

    public Article(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    public String getSummary(){
        if(this.content.length() < 50) {
            return this.content;
        }
        else{
            return this.content.substring(0, 50) + "...";
        }
    }
}
