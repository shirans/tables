package com.taboola.tables.db;

import javax.persistence.*;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Entity
public class TaboolaIdentity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name="user_id")
    private long userId;

    private String taboolaId;

    TaboolaIdentity() {

    }

    public TaboolaIdentity(long userId, String taboolaId) {
        this.userId = userId;
        this.taboolaId = taboolaId;
    }

    public long getUserId() {
        return userId;
    }

    public String getTaboolaId() {
        return taboolaId;
    }
}
