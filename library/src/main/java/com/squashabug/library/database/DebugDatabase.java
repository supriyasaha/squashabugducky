package com.squashabug.library.database;


import android.provider.BaseColumns;

public class DebugDatabase {

    public DebugDatabase() {
    }

    public static class EventsEntryTable implements BaseColumns {

        public static final String TABLE_NAME = "events";
        public static final String ENTRY_ID = "_id";
        public static final String EVENT_NAME = "eventName";
        public static final String PROPERTIES = "properties";
		public static final String PACKAGE_NAME = "package_name";
        public static final String TIMESTAMP = "timestamp";
    }

    public static class ApiLogTable implements BaseColumns {
        public static final String TABLE_NAME = "apiLog";
        public static final String ENTRY_ID = "_id";
        public static final String API_LOG = "apiData";
		public static final String PACKAGE_NAME = "package_name";
		public static final String TIMESTAMP = "timestamp";
    }

	public static class PackageTable implements BaseColumns{
		public static final String TABLE_NAME = "packageTable";
		public static final String PACKAGE = "packgae";

	}
}
