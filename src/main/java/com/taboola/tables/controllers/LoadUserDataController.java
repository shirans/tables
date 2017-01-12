package com.taboola.tables.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.h2.tools.Csv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.repackaged.com.google.common.base.Strings;
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
    UserSegmentRepo userSegmentRepo;

    @RequestMapping(value = "/admin/loaddata", method = RequestMethod.GET)
    public void load(@RequestParam(name = "path", defaultValue = "~/data.csv") String path) {

        userSegmentRepo.deleteAll();;

        int count= 0;
        try {
            ResultSet rs = new Csv().read(path, null, null);
            while (rs.next()) {
                count ++;
                String tid = rs.getString(1);
                String liverampSegment = rs.getString(2);
                if(!Strings.isNullOrEmpty(tid) && !Strings.isNullOrEmpty(liverampSegment)) {
                    userSegmentRepo.save(new UserSegmentData(tid, liverampSegment));
                }
            }
        } catch (SQLException e) {
            logger.error("An error occurred", e);
            e.printStackTrace();
        }

        List<UserSegmentData> all = Lists.newArrayList(userSegmentRepo.findAll());
        logger.info("num rows: " + count + "  num records: " + all.size());
        all.forEach(x -> logger.info(x.toString()));

    }
}


