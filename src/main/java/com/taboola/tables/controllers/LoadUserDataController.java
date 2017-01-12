package com.taboola.tables.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.tools.Csv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Lists;
import com.taboola.tables.db.UserSegmentData;
import com.taboola.tables.db.UserSegmentRepo;

/**
 * @author shiran.s on 1/12/17.
 */
@RestController
public class LoadUserDataController {

    private static final Logger logger = LoggerFactory.getLogger(LoadUserDataController.class);

    @Autowired
    UserSegmentRepo repo;

    @RequestMapping(value = "/admin/loaddata", method = RequestMethod.GET)
    public void load(@RequestParam(name = "path", defaultValue = "~/data.csv") String path) {

        Map<String,List<String>> map = new HashMap();

        try {
            ResultSet rs = new Csv().read(path, null, null);
            while (rs.next()) {
                String tid = rs.getString(1);
                String liverampSegment = rs.getString(2);
                List<String> strings = map.get(tid);
                if (strings == null) {
                    List<String> segments = Lists.newArrayList();
                    segments.add(liverampSegment);
                    map.put(tid,segments);
                }
                else {
                    strings.add(liverampSegment);
                }
//                repo.save(new UserSegmentData(tid, liverampSegment));
            }
        } catch (SQLException e) {

        }
        Iterable<UserSegmentData> all = repo.findAll();
        all.forEach(x -> logger.info(x.toString()));

    }
}


