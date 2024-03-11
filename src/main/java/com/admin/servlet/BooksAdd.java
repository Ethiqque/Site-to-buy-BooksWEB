//package com.admin.servlet;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.Part;
//
//import com.DAO.BookDAOImpl;
//import com.DB.DBConnect;
//import com.entity.BookDtls;
//
//@WebServlet("/add_books")
//@MultipartConfig
//public class BooksAdd extends HttpServlet {
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		try {
//			String bookName = req.getParameter("bname");
//			String author = req.getParameter("author");
//			String price = req.getParameter("price");
//			String categories = req.getParameter("categories");
//			String status = req.getParameter("status");
//			Part part = req.getPart("bimg");
//			String fileName = part.getSubmittedFileName();
//			String isbn = req.getParameter("isbn");
//			BookDtls b = new BookDtls(bookName, author, price, categories, status, fileName, "admin", isbn);
//
//			BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
//			HttpSession session = req.getSession();
//			if (fileName.endsWith(".pdf") || fileName.endsWith("docx")) {
//				boolean f = dao.addBooks(b);
//
//				if (f) {
//
//					String path = getServletContext().getRealPath("") + "book";
//
//					File file = new File(path);
//
//					part.write(path + File.separator + fileName);
//
//					session.setAttribute("succMsg", "Book Add Sucessfully");
//					resp.sendRedirect("admin/add_books.jsp");
//
//				} else {
//					session.setAttribute("failedMsg", "Something wrong on Server");
//					resp.sendRedirect("admin/add_books.jsp");
//				}
//			} else {
//				session.setAttribute("failedMsg", "invalid format!! please add pdf or docx file");
//				resp.sendRedirect("admin/add_books.jsp");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}

package com.admin.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDtls;

@WebServlet("/add_books")
@MultipartConfig
public class BooksAdd extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		try {
			String bookName = req.getParameter("bname");
			String author = req.getParameter("author");
			String price = req.getParameter("price");
			String categories = req.getParameter("categories");
			String status = req.getParameter("status");
			String isbn = req.getParameter("isbn");
			Part part = req.getPart("bimg");
			String fileName = part.getSubmittedFileName();

			if (!fileName.endsWith(".pdf") && !fileName.endsWith(".docx") && !fileName.endsWith(".jpg")) {
				session.setAttribute("failedMsg", "Invalid format! Please add a PDF or DOCX file.");
				resp.sendRedirect("admin/add_books.jsp");
				return;
			}

			BookDtls b = new BookDtls(bookName, author, price, categories, status, fileName, "admin", isbn);
			BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());

			boolean f = dao.addBooks(b);

			if (f) {
				String fileUploadPath = getServletContext().getInitParameter("fileUploadPath");
				File fileSaveDir = new File(fileUploadPath);
				if (!fileSaveDir.exists()) {
					fileSaveDir.mkdirs();
				}
				String fullPath = fileUploadPath + File.separator + fileName;
				System.out.println(fullPath);
				part.write(fullPath);

				session.setAttribute("succMsg", "Book Added Successfully.");
			} else {
				session.setAttribute("failedMsg", "Something wrong on server.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("failedMsg", "Server error: " + e.getMessage());
		}
		resp.sendRedirect("admin/add_books.jsp");
	}

//    private String determineNewFileName(File directory, String extension) {
//        int highestNumber = 0;
//        for (File file : directory.listFiles()) {
//            String name = file.getName();
//            if (name.startsWith("poster") && name.endsWith(extension)) {
//                int number = Integer.parseInt(name.substring(6, name.lastIndexOf('.')));
//                if (number > highestNumber) {
//                    highestNumber = number;
//                }
//            }
//        }
//        return "poster" + (highestNumber + 1) + extension;
}
