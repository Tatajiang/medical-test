<%@page import="com.jiang.medical.util.JsonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ page
	import="java.io.*"
	import="java.util.*"
	import="org.json.JSONObject"
	import="com.homolo.toolkit.filesystem.manager.*"
	import="com.homolo.toolkit.filesystem.*"
	import="org.apache.commons.io.*"
	import="com.homolo.portal.domain.*"
	import="com.homolo.portal.util.*"
	import="com.homolo.framework.util.UUID"
	import="java.text.SimpleDateFormat"
	import="org.apache.commons.fileupload.*"
	import="org.apache.commons.fileupload.disk.*" 
	import="org.apache.commons.fileupload.servlet.*"
	import="org.apache.commons.lang.StringUtils"
	import="com.homolo.framework.web.JspHelper"

%><%//文件保存目录路径

	JspHelper helper = new JspHelper(pageContext);
	FileSystemManager fileSystemManager = helper.getBean("tk.fileSystemManager", FileSystemManager.class);
	FileSystemPath fileSystemPath = helper.getBean(FileSystemPath.class);
	
	String savePath = fileSystemPath.getStorageDir() + "/upload/";

	//定义允许上传的文件扩展名
	String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp"};
	//最大文件大小
	long maxSize = 20 * 1024 * 1024;

	response.setContentType("text/html; charset=UTF-8");

	if (!ServletFileUpload.isMultipartContent(request)) {
		out.println(getError("请选择文件。"));
		return;
	}
	//检查目录
	File uploadDir = new File(fileSystemPath.getRealPath(savePath));
	if (uploadDir.exists() && !uploadDir.isDirectory()) {
		out.println(getError("上传目录不存在。"));
		return;
	}
	if (uploadDir.exists() && !uploadDir.canWrite()) {
		out.println(getError("上传目录没有写权限。"));
		return;
	}
	if (!uploadDir.exists()) {
		uploadDir.mkdirs();
	}

	//创建文件夹
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String ymd = sdf.format(new Date());
	savePath += ymd + "/";
	File dirFile = new File(fileSystemPath.getRealPath(savePath));
	if (!dirFile.exists()) {
		dirFile.mkdirs();
	}

	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	upload.setHeaderEncoding("UTF-8");
	List items = upload.parseRequest(request);
	Iterator itr = items.iterator();
	while (itr.hasNext()) {
		FileItem item = (FileItem) itr.next();
		String fileName = item.getName();
		long fileSize = item.getSize();
		if (!item.isFormField()) {
			//检查文件大小
			if (item.getSize() > maxSize) {
				out.println(getError("上传文件大小超过限制。"));
				return;
			}
			//检查扩展名
			String fileExt = FilenameUtils.getExtension(fileName.toLowerCase());
			if (!Arrays.<String> asList(fileTypes).contains(fileExt)) {
				out.println(getError("上传文件扩展名是不允许的扩展名。只支持jpg,jpeg,png,bmp,gif格式图片。"));
				return;
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileId = UUID.generateUUID();
			String newFileName = fileId + "." + fileExt;
			try {
				File uploadedFile = new File(fileSystemPath.getRealPath(savePath), newFileName);
				item.write(uploadedFile);
				
				com.homolo.toolkit.filesystem.domain.File f = new com.homolo.toolkit.filesystem.domain.File();
				f.setId(fileId);
				f.setName(fileName);
				f.setCreateDate(new Date());
				f.setSize(fileSize);
				f.setType(item.getContentType());
				f.setPath(savePath + "/" + newFileName);
				fileSystemManager.createFile(f);
			} catch (Exception e) {
				out.println(getError("上传文件失败。"));
				return;
			}
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("fileId", fileId);
			result.put("fileName", fileName);

			JSONObject obj = new JSONObject();
			obj.put("code", 0);
			obj.put("message", "上传成功！");
			obj.put("data", JsonUtil.getJsonStrFromEntity(result));
			out.println(obj.toString());
		}
	}
%>
<%!private String getError(String message) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("code", -1);
		obj.put("message", message);
		obj.put("data", "{}");
		return obj.toString();
	}%>