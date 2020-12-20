package com.sirma.factories;

import android.util.Log;

import com.sirma.model.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public final class RecordFactory {

    private static final String DEFAULT_REGEX_PATTERN = ", ";
    private static final String NULL_STR = "NULL";

    public RecordFactory() {
    }

    public static Record execute(String line) {
        String[] recordArgs = line.split(DEFAULT_REGEX_PATTERN);

        long emplID = Long.parseLong(recordArgs[0].trim());
        long projectID = Long.parseLong(recordArgs[1].trim());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFrom = null;
        try {
            dateFrom = format.parse(recordArgs[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateTo = null;

        if (recordArgs[3] == null || NULL_STR.equals(recordArgs[3])) {
            dateTo = new Date();
        } else {
            try {
                dateTo = format.parse(recordArgs[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Record(
                emplID,
                projectID,
                dateFrom,
                dateTo
        );
    }
}
