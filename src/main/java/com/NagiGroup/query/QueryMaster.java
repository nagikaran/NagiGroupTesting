package com.NagiGroup.query;

public final class QueryMaster {
	public static String user_login = "select * from user_login(?,?)";
	public static String insert_user = "SELECT insert_user(?,?,?,?,?,?,?,?,?,?,?)";
	public static String get_all_users = "SELECT * FROM public.get_all_users();";
	public static String get_all_sub_folders = "select * from get_all_sub_folders()";
	public static String insert_driver_document = "select * from insert_driver_document(?,?,?,?,?)";
	public static String get_all_driver_documents = "select * from get_all_driver_documents()";
	public static String get_driver_document_by_id = "select * from get_driver_document_by_id(?)";
	public static String load_insert= "select * from load_insert(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String load_Update= "select * from load_insert(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String load_document_insert= "select * from load_document_insert(?,?)";
	public static String load_document_update= "select * from load_document_insert(?,?)";
	public static String get_all_load = "SELECT * FROM public.get_all_load()";
	public static String get_load_by_id = "select * from get_load_by_id(?)";
	public static String load_status_history_insert= "select * from load_status_history_insert(?,?,?,?)";
	public static String current_load_status= "select * from current_load_status(?)";
	public static String updateLoadStatus= "select * from current_load_status(?)";
	public static String get_licence_key_by_id= "select * from get_licence_key_by_id(?)";;
}
