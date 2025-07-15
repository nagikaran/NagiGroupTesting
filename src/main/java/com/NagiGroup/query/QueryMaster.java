package com.NagiGroup.query;

public final class QueryMaster {

	
	public static String user_login = "select * from user_login(?,?)";
	public static String insert_user = "SELECT insert_user(?,?,?,?,?,?,?,?,?,?,?)";
	public static String get_all_users = "SELECT * FROM public.get_all_users();";
	public static String get_all_sub_folders = "select * from get_all_sub_folders()";
	public static String insert_driver_document = "select * from insert_driver_document(?,?,?,?,?,?)";
	public static String get_all_driver_documents = "select * from get_all_driver_documents()";
	public static String get_driver_document_by_id = "select * from get_driver_document_by_id(?)";
	public static String insert_load= "select * from insert_load(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String load_Update= "select * from load_insert(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insert_load_document= "select * from insert_load_document(?,?,?,?)";
	public static String load_document_update= "select * from load_document_insert(?,?)";
	public static String get_all_loads = "SELECT * FROM get_all_loads()";
	public static String get_load_by_id = "select * from get_load_by_id(?)";
	public static String insert_load_status_history= "select * from insert_load_status_history(?,?,?,?)";
	public static String current_load_status= "select * from current_load_status(?)";
	public static String updateLoadStatus= "select * from current_load_status(?)";
	public static String get_licence_key_by_id= "select * from get_licence_key_by_id(?)";;
	public static String get_load_status_id= "select * from get_load_status_id(?)";
	public static String update_load_driver = "select * from update_load_driver(?,?,?)";
	public static String update_load_source_pickup = "select * from update_load_source_pickup(?,?,?,?)";
	public static String update_load_destination_delivery= "select * from update_load_destination_delivery(?,?,?,?)";
	public static String update_load_final_price = "select * from update_load_final_price(?,?,?)";
	public static String update_load_base_price = "select * from update_load_base_price(?,?,?)";
	public static String update_loads_company_name = "select * from update_loads_company_name(?,?,?)";
	public static String update_load_trailer_used="select * from update_load_trailer_used(?,?,?)";
	public static String update_load_number="select * from update_load_number(?,?,?)";
	public static String update_load_status="select * from update_load_status(?,?,?,?)";
	public static String mark_load_in_progress="select * from mark_load_in_progress(?,?)";
	public static String handle_load_completion="select * from handle_load_completion(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insert_driver_document_with_month = "select * from insert_driver_document_with_month(?,?,?,?,?,?,?)";
	public static String get_all_company_details= "select * from get_all_company_details()";
	public static String get_company_details_by_id = "select * from get_company_details_by_id(?)";
	public static String get_next_invoice_number = "select * from get_next_invoice_number()";
	public static String get_loads_by_status = "select * from get_loads_by_status(?)";
	public static String get_load_status_summary = "select * from get_load_status_summary()";
	public static String get_company_list = "SELECT * FROM get_company_list()";
	public static String get_load_details_for_driver_assignment =  "SELECT * FROM get_load_details_for_driver_assignment(?,?)";
	public static String insert_invoice = "SELECT * FROM insert_invoice(?,?,?,?,?,?)";
	public static String update_load_with_charges_and_invoice = "SELECT * FROM update_load_with_charges_and_invoice(?,?,?,?,?,?,?,?)";
	public static String cancel_load_with_tonu =  "SELECT * FROM cancel_load_with_tonu(?,?,?,?,?)";
	public static String mark_documents_as_deleted = "SELECT * FROM mark_documents_as_deleted(?)";
	public static String get_invoice_number_by_load =  "SELECT * FROM get_invoice_number_by_load(?)";
	public static String update_load_number_by_load_id = "SELECT * FROM update_load_number_by_load_id(?,?,?,?)";
	public static String get_driver_monthly_summary = "SELECT * FROM get_driver_monthly_summary(?,?)";
	public static String get_driver_monthly_summary_for_loads = "SELECT * FROM get_driver_monthly_summary_for_loads(?,?)";
	public static String get_my_loads = "SELECT * FROM get_my_loads()";
	public static String get_loads_by_date = "SELECT * FROM get_loads_by_date(?)";
	public static String get_invoices_by_invoiced_status= "SELECT * FROM get_invoices_by_invoiced_status(?)";
	public static String get_paid_invoices= "SELECT * FROM get_paid_invoices()";
	public static String get_pending_charges_by_load_id= "SELECT * FROM get_pending_charges_by_load_id(?)";
	public static String get_invoice_number_by_load_number =  "SELECT * FROM get_invoice_number_by_load_number(?)";
	public static String cancel_load =  "SELECT * FROM cancel_load(?,?,?,?,?)";
	public static String mark_documents_as_deleted_by_id = "SELECT * FROM mark_documents_as_deleted_by_id(?)";
	public static String update_invoice_payment_status =  "SELECT * FROM update_invoice_payment_status(?,?,?,?)";
	public static String update_driver_document = "select * from update_driver_document(?,?,?,?,?,?,?)";
	public static String insert_driver_financial_summary = "select * from insert_driver_financial_summary(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String get_loads_by_destinations_order_as_per_load_type = "select * from get_loads_by_destinations_order_as_per_load_type(?)";
	public static String handle_load_completion_test="select * from handle_load_completion_test(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
}