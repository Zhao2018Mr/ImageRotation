package com.zyj.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片工具类
 */
public class ImageUtil {


	/**
	 * 获取exif信息
	 * @param inputStream
	 * @return
	 */
	public static Map<String, Object> getExif(InputStream inputStream) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
			map = printExif(metadata);
		} catch (ImageProcessingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	/**
	 * 获取exif信息，将旋转角度信息拿到
	 * @param metadata
	 * @return
	 */
	private static Map<String, Object> printExif(Metadata metadata) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tagName = null;
		String desc = null;
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				tagName = tag.getTagName();
				desc = tag.getDescription();
				if (tagName.equals("Orientation")) {
					map.put("Orientation", desc);
				}
			}
		}
		return map;
	}

	public static int getAngle(Map<String, Object> map) {
		int ro = 0;
		if(map.get("Orientation")!=null) {
			String ori = map.get("Orientation").toString();
			if (ori.indexOf("90") >= 0) {
				ro = 1;
			} else if (ori.indexOf("180") >= 0) {
				ro = 2;
			} else if (ori.indexOf("270") >= 0) {
				ro = 3;
			}
		}
		return ro;
	}

	public static BufferedImage getBufferedImg(BufferedImage src,int width, int height, int ro) {
		int angle = (int) (90 * ro);
		int type = src.getColorModel().getTransparency();
		int wid = width;
		int hei = height;
		if (ro % 2 != 0) {
			int temp = width;
			width = height;
			height = temp;
		}
		Rectangle re = new Rectangle(new Dimension(width, height));
		BufferedImage BfImg = null;
		BfImg = new BufferedImage(re.width, re.height, type);
		Graphics2D g2 = BfImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.rotate(Math.toRadians(angle), re.width / 2,re.height / 2);
		g2.drawImage(src, (re.width - wid) / 2, (re.height - hei) / 2, null);
		g2.dispose();
		return BfImg;
	}

	//获得图片的高
	public static int getHeight(InputStream is) {
		BufferedImage src = null;
		int height = -1;
		try {
			src = ImageIO.read(is);
			height = src.getHeight();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return height;
	}

	/**
	 * 获得图片的宽
	 * @param is
	 * @return
	 */
	public static int getWidth(InputStream is) {
		BufferedImage src = null;
		int width = -1;
		try {
			src = ImageIO.read(is);
			width = src.getWidth();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return width;
	}


	/**
	 *
	 * @param file 要旋转的图片
	 * @param dest 旋转完成保存的图片
	 * @param angle 旋转角度  1 ： 90  2:180  3:270
	 */
	public static void imageRotation(File file, File dest, int angle){
		String ex = file.getName().substring(file.getName().lastIndexOf("."));
		//图片翻转90
		try {
			InputStream inputStream= new FileInputStream(file);
			// MultipartFile 类型可以获取到长宽
			BufferedImage bf = ImageUtil.getBufferedImg(ImageIO.read(inputStream), ImageUtil.getWidth(inputStream), ImageUtil.getHeight(inputStream), angle);
			// file 文件 获取不到长宽
//			BufferedImage bf = ImageUtil.getBufferedImg(ImageIO.read(inputStream), 10000, 10000, angle);
			ImageIO.write(bf, ex.substring(1), dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File file = new File("D://smcftp/1.jpg");
		File dest = new File("D://smcftp/2.jpg");
		imageRotation(file,dest,1);
	}

}