# ImageRotation
图片旋转 主要方法
```java
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
```