package com.jiang.medical.util;

import com.homolo.framework.rest.ActionMethod;
import com.homolo.framework.rest.RequestParameters;
import com.homolo.framework.rest.RestService;
import com.homolo.framework.rest.ReturnResult;
import com.homolo.framework.service.ServiceResult;
import com.homolo.framework.util.MimeUtils;
import com.homolo.framework.util.UUID;
import com.homolo.toolkit.filesystem.FileSystemPath;
import com.homolo.toolkit.filesystem.manager.FileSystemManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

//import nl.siegmann.epublib.util.IOUtil;

/**
 * 文件处理
 * @author lq
 * Email: xiaolei_java@163.com 2015-11-3 上午10:21:35
 */
@RestService(name = "rs.File")
public class ResourceFileService {
	
	private Logger logger = Logger.getLogger(getClass());
		
	@Autowired
	private FileSystemPath fileSystemPath;
	
	@Autowired
	private FileSystemManager fileSystemManager;
		
	private long maxSize = 10 * 1024 * 1024;
	
	String[] imagesExts = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};
	
	/**
	 * 上传图片
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ActionMethod(response = "json")
	public Object upload(HttpServletRequest request){
		try{
		    FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			
			com.homolo.toolkit.filesystem.domain.File f = new com.homolo.toolkit.filesystem.domain.File();
			
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				long fileSize = item.getSize();
				if (!item.isFormField()) {
					//检查文件大小
					if (item.getSize() > maxSize) {
						return new ServiceResult(ReturnResult.FAILURE, "上传文件大小超过限制，最大不能超过" + StringUtil.formatFileSize(maxSize) + "。");
					}
					//检查扩展名
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					
					if(!Arrays.<String>asList(imagesExts).contains(fileExt)){
						return new ServiceResult(ReturnResult.FAILURE, "上传文件格式不正确！");
					}
					
					String savePath = fileSystemPath.getStorageDir() + File.separator + "APP";
					SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
				    savePath += File.separator + df.format(new Date());

				    File dirFile = new File(fileSystemPath.getRealPath(savePath));
			        if (!dirFile.exists()) {
			    		dirFile.mkdirs();
			    	}
					
					String fileId = UUID.generateUUID();
					String newFileName = fileId + "." + fileExt;
					
					File uploadedFile = new File(fileSystemPath.getRealPath(savePath), newFileName);
					item.write(uploadedFile);
					
					f = new com.homolo.toolkit.filesystem.domain.File();
					f.setId(fileId);
					f.setName(fileName);
					f.setSize(fileSize);
					f.setType(item.getContentType());
					f.setPath(savePath + File.separator + newFileName);
					f.setCreateDate(new Date());
					fileSystemManager.createFile(f);
				}
			}
			return new ServiceResult(ReturnResult.SUCCESS , "上传成功！", f);
		}catch (Exception e) {
			return new ServiceResult(ReturnResult.FAILURE, "上传失败！");
		}
	}
	
	/**
	 * 上传图片
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@ActionMethod(response = "json")
	public Object uploadByApp(HttpServletRequest request){
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new RetInfo(RetInfo.FAILURE, "请选择文件！");
		}
		String fileName = request.getParameter("fileName");
		if(StringUtils.isBlank(fileName)){
			return new RetInfo(RetInfo.FAILURE, "参数错误！[fileName]不能为空！");
		}
		
		com.homolo.toolkit.filesystem.domain.File f = null;
		
		InputStream is  = null;
        DataInputStream dis = null ;
        FileOutputStream fps = null;
        try {
        	is = request.getInputStream();
 	        dis = new DataInputStream(is);
 	        
 	        //检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			
			if(!Arrays.<String>asList(imagesExts).contains(fileExt)){
				return new RetInfo(RetInfo.FAILURE, "上传文件格式不正确！");
			}
			
			String savePath = fileSystemPath.getStorageDir() + File.separator + "APP";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		    savePath += File.separator + df.format(new Date());

		    File dirFile = new File(fileSystemPath.getRealPath(savePath));
	        if (!dirFile.exists()) {
	    		dirFile.mkdirs();
	    	}
	        String fileId = UUID.generateUUID();
			String newFileName = fileId + "." + fileExt;
			
			File uploadedFile = new File(fileSystemPath.getRealPath(savePath), newFileName);
			
 	        fps = new FileOutputStream(uploadedFile);
 	        int bufferSize = 1024;
 	        byte[] buffer = new byte[bufferSize];
 	        int length = -1;
 	        while ((length = dis.read(buffer)) > 0) {
               fps.write(buffer, 0, length);
 	        }
 	        
 	        f = new com.homolo.toolkit.filesystem.domain.File();
			f.setId(fileId);
			f.setName(fileName);
			f.setSize(buffer.length);
			f.setType("image/jpeg");
			f.setPath(savePath + File.separator + newFileName);
			f.setCreateDate(new Date());
			fileSystemManager.createFile(f);
			
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", f.getId());
			return new RetInfo(RetInfo.SUCCESS , "上传成功！", JsonUtil.getJsonStrFromEntity(item));
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	return new RetInfo(RetInfo.FAILURE, "上传文件发现异常导致失败！");
        }finally {
        	if(dis != null){
        		try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(is != null){
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if(fps != null){
        		try {
					fps.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
	}
	
	/**
	 * base64编码图片保存文件
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@SuppressWarnings("restriction")
	@ActionMethod(response = "json")
	public Object saveBase64File(RequestParameters reqParams , HttpServletRequest request){
		try{
			String fileName = request.getParameter("fileName");
			/*if(StringUtils.isBlank(fileName)){
				return new ServiceResult(ReturnResult.FAILURE, "文件名不能为空!");
			}
			if(fileName.length() > 20){
				return new ServiceResult(ReturnResult.FAILURE, "文件名不能超过20个字！");
			}*/
			String savePath = fileSystemPath.getStorageDir() + File.separator + "APP";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		    savePath += File.separator + df.format(new Date());

		    File dirFile = new File(fileSystemPath.getRealPath(savePath));
	        if (!dirFile.exists()) {
	    		dirFile.mkdirs();
	    	}

	        String fileId = UUID.generateUUID();
	        String newFileName = fileId + fileName;//默认格式为jpg
	        savePath += File.separator + newFileName;
	        
		    File writerFile = new File(fileSystemPath.getRealPath(savePath));
		    
			String pic_base_64_data = request.getParameter("picData");
			sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();
			
			if(null!=pic_base_64_data){
				byte[] datas = decode.decodeBuffer(pic_base_64_data.replaceAll(" ", "+"));
				long size = datas.length;
				if(size > maxSize){
					return new RetInfo(RetInfo.FAILURE, "上传文件大小超过限制,最大上传为" + StringUtil.formatFileSize(maxSize) + "。");
				}
				OutputStream fos = new FileOutputStream(writerFile);
				fos.write(datas);
				fos.flush();
				fos.close();
			}else{
				return new RetInfo(ReturnResult.FAILURE, "请选择文件！");
			}
			
			com.homolo.toolkit.filesystem.domain.File f = new com.homolo.toolkit.filesystem.domain.File();
			f.setId(fileId);
			f.setName(newFileName);
			f.setCreateDate(new Date());
			f.setSize(writerFile.length());
			f.setType(MimeUtils.getMimeType(newFileName));
			f.setPath(savePath);
			fileSystemManager.createFile(f);
			
			//更新买家信息
//			Buyer buyer = (Buyer) request.getSession().getAttribute("_buyer");
//			buyer.setPictrueId(f.getId());
//			buyerManager.updateObject(buyer);
//			request.getSession().setAttribute("_buyer", buyer);

			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", f.getId());
			return new RetInfo(RetInfo.SUCCESS , "上传成功！", JsonUtil.getJsonStrFromEntity(item));
		}catch (Exception e) {
			return new RetInfo(RetInfo.FAILURE, "上传失败！");
		}
	}
	
	/**
	 * base64编码图片保存文件
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@SuppressWarnings("restriction")
	@ActionMethod(response = "json")
	public Object saveBase64FileByPM(RequestParameters reqParams , HttpServletRequest request){
		try{
//			User user = SessionUtil.getCurrentUser(request);
//			if(user == null){
//				return new ServiceResult(ReturnResult.FAILURE, "请先登录！");
//			}
			String owner = request.getParameter("owner");
			String fileName = request.getParameter("fileName");
			if(StringUtils.isBlank(fileName)){
				return new ServiceResult(ReturnResult.FAILURE, "文件名不能为空!");
			}
			/*if(fileName.length() > 20){
				return new ServiceResult(ReturnResult.FAILURE, "文件名不能超过20个字！");
			}*/
			String ext = ".jpg", name = fileName;
			if(StringUtils.contains(fileName, ".")){
				ext = "." + StringUtils.substringAfterLast(fileName, ".");
				name = StringUtils.substringBeforeLast(fileName, ".");
			}
			String savePath = fileSystemPath.getStorageDir() + File.separator + "pointMall";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		    savePath += File.separator + df.format(new Date());

		    File dirFile = new File(fileSystemPath.getRealPath(savePath));
	        if (!dirFile.exists()) {
	    		dirFile.mkdirs();
	    	}

	        String fileId = UUID.generateUUID();
	        String newFileName = fileId + ext;//默认格式为jpg
	        savePath += File.separator + newFileName;
	        
		    File writerFile = new File(fileSystemPath.getRealPath(savePath));
		    
			String pic_base_64_data = request.getParameter("picData");
			sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();
			
			if(null!=pic_base_64_data){
				byte[] datas = decode.decodeBuffer(pic_base_64_data.replaceAll(" ", "+"));
				long size = datas.length;
				if(size > 2 * 1024 * 1024L){
					return new ServiceResult(ReturnResult.FAILURE, "上传文件大小超过限制,最大上传为" + StringUtil.formatFileSize(2 * 1024 * 1024L) + "。");
				}
				OutputStream fos = new FileOutputStream(writerFile);
				fos.write(datas);
				fos.flush();
				fos.close();
			}else{
				return new ServiceResult(ReturnResult.FAILURE, "请选择文件！");
			}
			
			com.homolo.toolkit.filesystem.domain.File f = new com.homolo.toolkit.filesystem.domain.File();
			f.setId(fileId);
			f.setOwner(owner);
			f.setName(name);
			f.setCreateDate(new Date());
			f.setSize(writerFile.length());
			f.setType(MimeUtils.getMimeType(fileName));
			f.setPath(savePath);
			fileSystemManager.createFile(f);
			
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", f.getId());
			return new ServiceResult(ReturnResult.SUCCESS, "上传成功！", item);
		}catch (Exception e) {
			return new ServiceResult(ReturnResult.FAILURE, "上传失败！");
		}
	}
	
	/**
	 * 根据fileId删除文件数据及文件
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@ActionMethod(response = "json")
	public Object deleteFileById(RequestParameters reqParams , HttpServletRequest request){
		String fileId = reqParams.getParameter("fileId", String.class);
		try{
			com.homolo.toolkit.filesystem.domain.File obj = fileSystemManager.getFile(fileId);
			if(obj == null){
				return new ServiceResult(ReturnResult.FAILURE, "对象不存在！");
			}
			File file = new File(fileSystemPath.getRealPath(obj.getPath()));
			file.delete();
			fileSystemManager.deleteFile(obj);
			
			return new RetInfo(RetInfo.SUCCESS, "删除成功！");
		}catch (Exception e) {
			return new RetInfo(RetInfo.FAILURE, "删除失败！");
		}
	}
	
	/**
	 * 下载文件
	 * @param reqParams
	 * @param request
	 * @return
	 */
	@ActionMethod(response = "stream")
	public Object download(RequestParameters reqParams, HttpServletResponse response){
		String fileId = reqParams.getParameter("id", String.class);
		
		OutputStream out = null;
		try{
			com.homolo.toolkit.filesystem.domain.File obj = fileSystemManager.getFile(fileId);
			if(obj == null) 
				return new ServiceResult(ReturnResult.NOT_FOUND_OBJECT, "对象不存在！");
			
			File file = new File(fileSystemPath.getRealPath(obj.getPath()));
			if(!file.exists())
				return new ServiceResult(ReturnResult.NOT_FOUND_OBJECT, "文件不存在！");
			
			byte[] fileNameByte = (obj.getName() + "").getBytes("GBK");
			String fullFileName = new String(fileNameByte, "ISO8859-1");
			
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment;filename=" + fullFileName);
			response.addHeader("Content-Length", "" + file.length());
            
			out = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            out.write(FileUtils.readFileToByteArray(file));
            out.flush();
            out.close();
            return new ServiceResult(ReturnResult.SUCCESS, "下载成功！");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResult(ReturnResult.FAILURE, "下载失败！");
		}finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
