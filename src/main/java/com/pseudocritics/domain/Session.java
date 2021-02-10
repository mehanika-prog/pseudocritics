package com.pseudocritics.domain;


import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity(name = "Session")
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "expire_time", nullable = false)
    private OffsetDateTime offsetDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public OffsetDateTime getOffsetDateTime() { return offsetDateTime; }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) { this.offsetDateTime = offsetDateTime; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

}
