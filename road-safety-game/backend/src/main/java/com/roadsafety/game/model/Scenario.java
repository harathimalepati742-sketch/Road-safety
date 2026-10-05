package com.roadsafety.game.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** A road-safety situation with several possible actions. */
@Entity
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String category;

    private String emoji;

    /** Safety guidance shown after the child answers. */
    @Column(nullable = false, length = 500)
    private String tip;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id")
    private List<AnswerOption> options = new ArrayList<>();

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
    public List<AnswerOption> getOptions() { return options; }
}
