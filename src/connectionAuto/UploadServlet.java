package connectionAuto;

import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
 

public class UploadServlet extends HttpServlet {
	private static final String UPLOAD_DIRECTORY = "data";
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	String value;
	
	
	@Override
	public void init() throws ServletException{
		ServletConfig config = getServletConfig();
		 value = config.getServletName();
		 
		 
	}
	
	protected void processRequest
	(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	/*	PrintWriter writer = response.getWriter();
		File f = new File(".");
		File[] files = f.listFiles();
		
		for(File file : files){
			if(file.isDirectory()){
				writer.println("Directory: ");
			}
			else{
				writer.println("    File:");
			}
			writer.println(file.getCanonicalPath());
		}
		
		try{
			writer.println(value);		
		}
		finally {
			
		}
		*/
	}
	
	protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		//checks if the request actually contains upload file'
		
		 if (!ServletFileUpload.isMultipartContent(request)) {
	            PrintWriter writer = response.getWriter();
	            writer.println("Request does not contain upload data");
	            writer.flush();
	            return;
	        }
	         
	        // configures upload settings
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        factory.setSizeThreshold(THRESHOLD_SIZE);
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	         
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	         
	        // constructs the directory path to store upload file
	        String uploadPath = getServletContext().getRealPath("")
	            + File.separator + UPLOAD_DIRECTORY;
	        // creates the directory if it does not exist
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
	         
	        try {
	            // parses the request's content to extract file data
	        	List formItems = upload.parseRequest(new ServletRequestContext(request));
	            Iterator iter = formItems.iterator();
	            
	             
	            // iterates over form's fields
	            while (iter.hasNext()) {
	                FileItem item = (FileItem) iter.next();
	                // processes only fields that are not form fields
	                if (!item.isFormField()) {
	                    String fileName = new File(item.getName()).getName();
	                    String filePath = uploadPath + File.separator + fileName;
	                    File storeFile = new File(filePath);
	                     
	                    // saves the file on disk
	                    item.write(storeFile);
	                }
	            }
	            request.setAttribute("message", "Upload has been done successfully!");
	        } catch (Exception ex) {
	            request.setAttribute("message", "There was an error: " + ex.getMessage());
	        }
	        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
	    }
		
	}     

	


