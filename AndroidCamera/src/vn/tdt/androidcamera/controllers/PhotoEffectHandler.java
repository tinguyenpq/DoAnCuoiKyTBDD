package vn.tdt.androidcamera.controllers;

import vn.tdt.androidcamera.models.ConvolutionMatrix;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class PhotoEffectHandler {
	public static final int FLIP_VERTICAL = 1;
	public static final int FLIP_HORIZONTAL = 2;
	
		// red, blue, green
		public static Bitmap createSepiaToningEffect(Bitmap src, double depth2,
				double red, double green, double blue) {
			int width = src.getWidth();
			int height = src.getHeight();
			Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
			Canvas c = new Canvas();
			c.setBitmap(bmOut);
			c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));

			final double GS_RED = 0.3;
			final double GS_GREEN = 0.59;
			final double GS_BLUE = 0.11;
			int A, R, G, B;
			int pixel;

			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					pixel = src.getPixel(x, y);
					A = Color.alpha(pixel);
					R = Color.red(pixel);
					G = Color.green(pixel);
					B = Color.blue(pixel);
					// apply grayscale sample
					// B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

					R += (depth2 * red);
					if (R > 255) {
						R = 255;
					}

					G += (depth2 * green);
					if (G > 255) {
						G = 255;
					}

					B += (depth2 * blue);
					if (B > 255) {
						B = 255;
					}

					bmOut.setPixel(x, y, Color.argb(A, R, G, B));
				}
			}
			return bmOut;
		}

		public static Bitmap adjustedContrast(Bitmap src, double value) {
			int width = src.getWidth();
			int height = src.getHeight();
			Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

			Canvas c = new Canvas();
			c.setBitmap(bmOut);

			c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));

			int A, R, G, B;
			int pixel;
			double contrast = Math.pow((100 + value) / 100, 2);

			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					pixel = src.getPixel(x, y);
					A = Color.alpha(pixel);
					R = Color.red(pixel);
					R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
					if (R < 0) {
						R = 0;
					} else if (R > 255) {
						R = 255;
					}

					G = Color.green(pixel);
					G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
					if (G < 0) {
						G = 0;
					} else if (G > 255) {
						G = 255;
					}

					B = Color.blue(pixel);
					B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
					if (B < 0) {
						B = 0;
					} else if (B > 255) {
						B = 255;
					}

					bmOut.setPixel(x, y, Color.argb(A, R, G, B));
				}
			}
			return bmOut;
		}

		public static Bitmap doBrightness(Bitmap src, int value) {
			int width = src.getWidth();
			int height = src.getHeight();
			Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
			int A, R, G, B;
			int pixel;

			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					pixel = src.getPixel(x, y);
					A = Color.alpha(pixel);
					R = Color.red(pixel);
					G = Color.green(pixel);
					B = Color.blue(pixel);

					R += value;
					if (R > 255) {
						R = 255;
					} else if (R < 0) {
						R = 0;
					}

					G += value;
					if (G > 255) {
						G = 255;
					} else if (G < 0) {
						G = 0;
					}

					B += value;
					if (B > 255) {
						B = 255;
					} else if (B < 0) {
						B = 0;
					}

					bmOut.setPixel(x, y, Color.argb(A, R, G, B));
				}
			}

			return bmOut;
		}

		public static Bitmap doInvert(Bitmap src) {
			Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
					src.getConfig());
			int A, R, G, B;
			int pixelColor;
			int height = src.getHeight();
			int width = src.getWidth();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					pixelColor = src.getPixel(x, y);
					A = Color.alpha(pixelColor);
					R = 255 - Color.red(pixelColor);
					G = 255 - Color.green(pixelColor);
					B = 255 - Color.blue(pixelColor);
					bmOut.setPixel(x, y, Color.argb(A, R, G, B));
				}
			}

			return bmOut;
		}
		
		public static Bitmap doHighlightImage(Bitmap src) {
		    Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(bmOut);
		    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		 
		    Paint ptBlur = new Paint();
		    ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
		    int[] offsetXY = new int[2];
		    Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
		    Paint ptAlphaColor = new Paint();
		    ptAlphaColor.setColor(0xFFFFFFFF);
		    canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
		    // free memory
		    bmAlpha.recycle();
		 
		    canvas.drawBitmap(src, 0, 0, null);
		 
		    return bmOut;
		}
		
		// just black and white
		public static Bitmap doGreyscale(Bitmap src) {
		    final double GS_RED = 0.299;
		    final double GS_GREEN = 0.587;
		    final double GS_BLUE = 0.114;
		 
		    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		    int A, R, G, B;
		    int pixel;
		 
		    int width = src.getWidth();
		    int height = src.getHeight();
		 
		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		            pixel = src.getPixel(x, y);
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
		            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
		        }
		    }
		 
		    return bmOut;
		}
		
		public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
		    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		    int width = src.getWidth();
		    int height = src.getHeight();
		    int A, R, G, B;
		    int pixel;
		    final int    MAX_SIZE = 256;
		    final double MAX_VALUE_DBL = 255.0;
		    final int    MAX_VALUE_INT = 255;
		    final double REVERSE = 10.0;
		 
		    int[] gammaR = new int[MAX_SIZE];
		    int[] gammaG = new int[MAX_SIZE];
		    int[] gammaB = new int[MAX_SIZE];
		 
		    for(int i = 0; i < MAX_SIZE; ++i) {
		        gammaR[i] = (int)Math.min(MAX_VALUE_INT,
		                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
		        gammaG[i] = (int)Math.min(MAX_VALUE_INT,
		                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
		        gammaB[i] = (int)Math.min(MAX_VALUE_INT,
		                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
		    }
		 
		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		            pixel = src.getPixel(x, y);
		            A = Color.alpha(pixel);
		            R = gammaR[Color.red(pixel)];
		            G = gammaG[Color.green(pixel)];
		            B = gammaB[Color.blue(pixel)];
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
		        }
		    }
		 
		    return bmOut;
		}

		public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset) {
		    int width = src.getWidth();
		    int height = src.getHeight();
		    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		    int A, R, G, B;
		    int pixel;
		 
		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		            pixel = src.getPixel(x, y);
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
		 
		            // round-off color offset
		            R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
		            if(R < 0) { R = 0; }
		            G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
		            if(G < 0) { G = 0; }
		            B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
		            if(B < 0) { B = 0; }
		 
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
		        }
		    }
		 
		    return bmOut;
		}

		public static Bitmap rotate(Bitmap src, float degree) {
		    Matrix matrix = new Matrix();
		    matrix.postRotate(degree);
		    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		}
		
		public static Bitmap boost(Bitmap src, int type, float percent) {
		    int width = src.getWidth();
		    int height = src.getHeight();
		    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		 
		    int A, R, G, B;
		    int pixel;
		 
		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		            pixel = src.getPixel(x, y);
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
		            if(type == 1) {
		                R = (int)(R * (1 + percent));
		                if(R > 255) R = 255;
		            }
		            else if(type == 2) {
		                G = (int)(G * (1 + percent));
		                if(G > 255) G = 255;
		            }
		            else if(type == 3) {
		                B = (int)(B * (1 + percent));
		                if(B > 255) B = 255;
		            }
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
		        }
		    }
		    return bmOut;
		}

		public static Bitmap mark(Bitmap src, String watermark, Point location, int color, int alpha, int size, boolean underline) {
		    int w = src.getWidth();
		    int h = src.getHeight();
		    Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		 
		    Canvas canvas = new Canvas(result);
		    canvas.drawBitmap(src, 0, 0, null);
		 
		    Paint paint = new Paint();
		    paint.setColor(color);
		    paint.setAlpha(alpha);
		    paint.setTextSize(size);
		    paint.setAntiAlias(true);
		    paint.setUnderlineText(underline);
		    canvas.drawText(watermark, location.x, location.y, paint);
		 
		    return result;
		}

		public static Bitmap flip(Bitmap src, int type) {
		    Matrix matrix = new Matrix();
		    if(type == FLIP_VERTICAL) {
		        // y = y * -1
		        matrix.preScale(1.0f, -1.0f);
		    }
		    else if(type == FLIP_HORIZONTAL) {
		        // x = x * -1
		        matrix.preScale(-1.0f, 1.0f);
		    } else {
		        return null;
		    }
		 
		    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		}

		public static Bitmap applyHueFilter(Bitmap source, int level) {
		    int width = source.getWidth();
		    int height = source.getHeight();
		    int[] pixels = new int[width * height];
		    float[] HSV = new float[3];
		    source.getPixels(pixels, 0, width, 0, 0, width, height);
		     
		    int index = 0;
		    for(int y = 0; y < height; ++y) {
		        for(int x = 0; x < width; ++x) {
		            // get current index in 2D-matrix
		            index = y * width + x;              
		            // convert to HSV
		            Color.colorToHSV(pixels[index], HSV);
		            // increase Saturation level
		            HSV[0] *= level;
		            HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
		            // take color back
		            pixels[index] |= Color.HSVToColor(HSV);
		        }
		    }
		    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		    return bmOut;       
		}
		
		public static Bitmap applySaturationFilter(Bitmap source, double level) {
			// get original image size
			int width = source.getWidth();
			int height = source.getHeight();
			int[] pixels = new int[width * height];
			float[] HSV = new float[3];
			// get pixel array from source image
			source.getPixels(pixels, 0, width, 0, 0, width, height);

			int index = 0;
			// iteration through all pixels
			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					// get current index in 2D-matrix
					index = y * width + x;
					// convert to HSV
					Color.colorToHSV(pixels[index], HSV);
					// increase Saturation level
					HSV[1] *= level;
					HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
					// take color back
					pixels[index] = Color.HSVToColor(HSV);
				}
			}
			// output bitmap
			Bitmap bmOut = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
			return bmOut;
		}
		// make shadow
		public static Bitmap applyReflection(Bitmap originalImage) {
		    // gap space between original and reflected
		    final int reflectionGap = 4;
		    // get image size
		    int width = originalImage.getWidth();
		    int height = originalImage.getHeight();          
		 
		    // this will not scale but will flip on the Y axis
		    Matrix matrix = new Matrix();
		    matrix.preScale(1, -1);
		       
		    // create a Bitmap with the flip matrix applied to it.
		    // we only want the bottom half of the image
		    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);          
		           
		    // create a new bitmap with same width but taller to fit reflection
		    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
		     
		    // create a new Canvas with the bitmap that's big enough for
		    // the image plus gap plus reflection
		    Canvas canvas = new Canvas(bitmapWithReflection);
		    // draw in the original image
		    canvas.drawBitmap(originalImage, 0, 0, null);
		    // draw in the gap
		    Paint defaultPaint = new Paint();
		    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		    // draw in the reflection
		    canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
		      
		    // create a shader that is a linear gradient that covers the reflection
		    Paint paint = new Paint();
		    LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
		            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
		            TileMode.CLAMP);
		    // set the paint to use this shader (linear gradient)
		    paint.setShader(shader);
		    // set the Transfer mode to be porter duff and destination in
		    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		    // draw a rectangle using the paint with our linear gradient
		    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		             
		    return bitmapWithReflection;
		}
		
		public static Bitmap applyGaussianBlur(Bitmap src, int red, int green) {
			// double[][] GaussianBlurConfig = new double[][] { { 1, 2, 1 },
			// { 2, 4, 2 }, { 1, 2, 1 } };
			// 1 2 1
			// 2 4 2
			// 1 2 1
			// double[][] GaussianBlurConfig = new double[][] { { -1, -1, -1 },
			// { -1, 9, -1 }, { -1, -1, -1 } };
			double GaussianBlurConfig[][] = { { 1, 1, 1 }, { 1, 5, 1 }, { 1, 1, 1 } };
			// double GaussianBlurConfig[][] = { { 0.01, 0.08, 0.01 },
			// { 0.08, 0.64, 0.08 }, { 0.01, 0.08, 0.01 } };
			ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
			convMatrix.applyConfig(GaussianBlurConfig);
			convMatrix.Factor = red;
			convMatrix.Offset = green;
			return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
		}
}
