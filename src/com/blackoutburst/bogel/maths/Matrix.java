package com.blackoutburst.bogel.maths;

import com.blackoutburst.bogel.core.Display;

import java.nio.FloatBuffer;

public class Matrix {

	public float m00; 
	public float m01; 
	public float m02; 
	public float m03; 
	public float m10; 
	public float m11; 
	public float m12; 
	public float m13; 
	public float m20; 
	public float m21; 
	public float m22; 
	public float m23; 
	public float m30; 
	public float m31; 
	public float m32; 
	public float m33;
	
	public Matrix() {
		setIdentity(this);
	}

	public static Matrix projectionMatrix(float FOV, float FAR_PLANE, float NEAR_PLANE, Matrix m){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		m.m00 = x_scale;
		m.m11 = y_scale;
		m.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		m.m23 = -1;
		m.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		m.m33 = 0;
		return (m);
	}

	/**
	 * Returns a string representation of this matrix
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append('[').append(m00).append(' ').append(m10).append(' ').append(m20).append(' ').append(m30).append(']').append('\n');
		buf.append('[').append(m01).append(' ').append(m11).append(' ').append(m21).append(' ').append(m31).append(']').append('\n');
		buf.append('[').append(m02).append(' ').append(m12).append(' ').append(m22).append(' ').append(m32).append(']').append('\n');
		buf.append('[').append(m03).append(' ').append(m13).append(' ').append(m23).append(' ').append(m33).append(']').append('\n');
		return buf.toString();
	}
	
	public static float[] getValues(Matrix m) {
		return(new float[] {m.m00, m.m01, m.m02, m.m03, 
							m.m10, m.m11, m.m12, m.m13, 
							m.m20, m.m21, m.m22, m.m23, 
							m.m30, m.m31, m.m32, m.m33});
	}
	
	public static void setIdentity(Matrix m) {
		m.m00 = 1.0f;
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m03 = 0.0f;
		m.m10 = 0.0f;
		m.m11 = 1.0f;
		m.m12 = 0.0f;
		m.m13 = 0.0f;
		m.m20 = 0.0f;
		m.m21 = 0.0f;
		m.m22 = 1.0f;
		m.m23 = 0.0f;
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = 0.0f;
		m.m33 = 1.0f;
	}
	
	public static void ortho2D(Matrix m, float left, float right, float bottom, float top, float near, float far) {
		float x_orth = 2.0f / (right - left);
		float y_orth = 2.0f / (top - bottom);
		float z_orth = -2.0f / (far - near);

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		m.m00 = x_orth;
		m.m10 = 0;
		m.m20 = 0;
		m.m30 = tx;
		m.m01 = 0;
		m.m11 = y_orth;
		m.m21 = 0;
		m.m31 = ty;
		m.m02 = 0;
		m.m12 = 0;
		m.m22 = z_orth;
		m.m32 = tz;
		m.m03 = 0;
		m.m13 = 0;
		m.m23 = 0;
		m.m33 = 1;
	}
	
	/**
	 * Set the given matrix to 0.
	 * @param m The matrix to set to 0
	 * @return m
	 */
	public static Matrix setZero(Matrix m) {
		m.m00 = 0.0f;
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m03 = 0.0f;
		m.m10 = 0.0f;
		m.m11 = 0.0f;
		m.m12 = 0.0f;
		m.m13 = 0.0f;
		m.m20 = 0.0f;
		m.m21 = 0.0f;
		m.m22 = 0.0f;
		m.m23 = 0.0f;
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = 0.0f;
		m.m33 = 0.0f;

		return m;
	}

	/**
	 * Load from another matrix4f
	 * @param src The source matrix
	 * @return this
	 */
	public Matrix load(Matrix src) {
		return load(src, this);
	}

	/**
	 * Copy the source matrix to the destination matrix
	 * @param src The source matrix
	 * @param dest The destination matrix, or null of a new one is to be created
	 * @return The copied matrix
	 */
	public static Matrix load(Matrix src, Matrix dest) {
		if (dest == null)
			dest = new Matrix();
		dest.m00 = src.m00;
		dest.m01 = src.m01;
		dest.m02 = src.m02;
		dest.m03 = src.m03;
		dest.m10 = src.m10;
		dest.m11 = src.m11;
		dest.m12 = src.m12;
		dest.m13 = src.m13;
		dest.m20 = src.m20;
		dest.m21 = src.m21;
		dest.m22 = src.m22;
		dest.m23 = src.m23;
		dest.m30 = src.m30;
		dest.m31 = src.m31;
		dest.m32 = src.m32;
		dest.m33 = src.m33;

		return dest;
	}

	/**
	 * Load from a float buffer. The buffer stores the matrix in column major
	 * (OpenGL) order.
	 *
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public Matrix load(FloatBuffer buf) {

		m00 = buf.get();
		m01 = buf.get();
		m02 = buf.get();
		m03 = buf.get();
		m10 = buf.get();
		m11 = buf.get();
		m12 = buf.get();
		m13 = buf.get();
		m20 = buf.get();
		m21 = buf.get();
		m22 = buf.get();
		m23 = buf.get();
		m30 = buf.get();
		m31 = buf.get();
		m32 = buf.get();
		m33 = buf.get();

		return this;
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in column
	 * major (openGL) order.
	 * @param buf The buffer to store this matrix in
	 */
	public Matrix store(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m03);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m13);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
		buf.put(m23);
		buf.put(m30);
		buf.put(m31);
		buf.put(m32);
		buf.put(m33);
		return this;
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in row
	 * major (maths) order.
	 * @param buf The buffer to store this matrix in
	 */
	public Matrix storeTranspose(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m10);
		buf.put(m20);
		buf.put(m30);
		buf.put(m01);
		buf.put(m11);
		buf.put(m21);
		buf.put(m31);
		buf.put(m02);
		buf.put(m12);
		buf.put(m22);
		buf.put(m32);
		buf.put(m03);
		buf.put(m13);
		buf.put(m23);
		buf.put(m33);
		return this;
	}

	/**
	 * Add two matrices together and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix add(Matrix left, Matrix right, Matrix dest) {
		if (dest == null)
			dest = new Matrix();

		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m02 = left.m02 + right.m02;
		dest.m03 = left.m03 + right.m03;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;
		dest.m12 = left.m12 + right.m12;
		dest.m13 = left.m13 + right.m13;
		dest.m20 = left.m20 + right.m20;
		dest.m21 = left.m21 + right.m21;
		dest.m22 = left.m22 + right.m22;
		dest.m23 = left.m23 + right.m23;
		dest.m30 = left.m30 + right.m30;
		dest.m31 = left.m31 + right.m31;
		dest.m32 = left.m32 + right.m32;
		dest.m33 = left.m33 + right.m33;

		return dest;
	}

	/**
	 * Subtract the right matrix from the left and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix sub(Matrix left, Matrix right, Matrix dest) {
		if (dest == null)
			dest = new Matrix();

		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m02 = left.m02 - right.m02;
		dest.m03 = left.m03 - right.m03;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;
		dest.m12 = left.m12 - right.m12;
		dest.m13 = left.m13 - right.m13;
		dest.m20 = left.m20 - right.m20;
		dest.m21 = left.m21 - right.m21;
		dest.m22 = left.m22 - right.m22;
		dest.m23 = left.m23 - right.m23;
		dest.m30 = left.m30 - right.m30;
		dest.m31 = left.m31 - right.m31;
		dest.m32 = left.m32 - right.m32;
		dest.m33 = left.m33 - right.m33;

		return dest;
	}

	/**
	 * Multiply the right matrix by the left and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix mul(Matrix left, Matrix right, Matrix dest) {
		if (dest == null)
			dest = new Matrix();

		float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
		float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
		float m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
		float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
		float m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
		float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
		float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
		float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
		float m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
		float m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
		float m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
		float m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
		float m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	/**
	 * Transpose this matrix
	 * @return this
	 */
	public Matrix transpose() {
		return transpose(this);
	}

	/**
	 * Scales the source matrix and put the result in the destination matrix
	 * @param vec The vector to scale by
	 * @return The scaled matrix
	 */
	public static void scale(Vector2f vec, Matrix mat) {
		if (mat == null)
			mat = new Matrix();
		mat.m00 = mat.m00 * vec.x;
		mat.m01 = mat.m01 * vec.x;
		mat.m02 = mat.m02 * vec.x;
		mat.m03 = mat.m03 * vec.x;
		mat.m10 = mat.m10 * vec.y;
		mat.m11 = mat.m11 * vec.y;
		mat.m12 = mat.m12 * vec.y;
		mat.m13 = mat.m13 * vec.y;
		mat.m20 = mat.m20 * 1;
		mat.m21 = mat.m21 * 1;
		mat.m22 = mat.m22 * 1;
		mat.m23 = mat.m23 * 1;
	}

	/**
	 * Scales the source matrix and put the result in the destination matrix
	 * @param vec The vector to scale by
	 * @return The scaled matrix
	 */
	public static void scale(Vector3f vec, Matrix mat) {
		if (mat == null)
			mat = new Matrix();
		mat.m00 = mat.m00 * vec.x;
		mat.m01 = mat.m01 * vec.x;
		mat.m02 = mat.m02 * vec.x;
		mat.m03 = mat.m03 * vec.x;
		mat.m10 = mat.m10 * vec.y;
		mat.m11 = mat.m11 * vec.y;
		mat.m12 = mat.m12 * vec.y;
		mat.m13 = mat.m13 * vec.y;
		mat.m20 = mat.m20 * vec.z;
		mat.m21 = mat.m21 * vec.z;
		mat.m22 = mat.m22 * vec.z;
		mat.m23 = mat.m23 * vec.z;
	}


	/**
	 * Rotates the source matrix around the given axis the specified angle and
	 * put the result in the destination matrix.
	 * @param angle the angle, in radians.
	 * @return The rotated matrix
	 */
	public static void rotate(float angle, Matrix mat) {
		if (mat == null)
			mat = new Matrix();
		Matrix src = new Matrix();
		Matrix.load(mat, src);
		
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = 0;
		float yz = 0;
		float xz = 0;
		float xs = 0;
		float ys = 0;
		float zs = 1*s;

		float f00 = 0*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		// n[3] not used
		float f10 = xy*oneminusc-zs;
		float f11 = 0*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		// n[7] not used
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = 1*oneminusc+c;

		float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		mat.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		mat.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		mat.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		mat.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		mat.m00 = t00;
		mat.m01 = t01;
		mat.m02 = t02;
		mat.m03 = t03;
		mat.m10 = t10;
		mat.m11 = t11;
		mat.m12 = t12;
		mat.m13 = t13;
	}


	/**
	 * Rotates the source matrix around the given axis the specified angle and
	 * put the result in the destination matrix.
	 * @param angle the angle, in radians.
	 * @param axis The vector representing the rotation axis. Must be normalized.
	 * @return The rotated matrix
	 */
	public static void rotate(float angle, Vector3f axis, Matrix mat) {
		if (mat == null)
			mat = new Matrix();

		Matrix src = new Matrix();
		Matrix.load(mat, src);

		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x*axis.y;
		float yz = axis.y*axis.z;
		float xz = axis.x*axis.z;
		float xs = axis.x*s;
		float ys = axis.y*s;
		float zs = axis.z*s;

		float f00 = axis.x*axis.x*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		// n[3] not used
		float f10 = xy*oneminusc-zs;
		float f11 = axis.y*axis.y*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		// n[7] not used
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.z*axis.z*oneminusc+c;

		float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		mat.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		mat.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		mat.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		mat.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		mat.m00 = t00;
		mat.m01 = t01;
		mat.m02 = t02;
		mat.m03 = t03;
		mat.m10 = t10;
		mat.m11 = t11;
		mat.m12 = t12;
		mat.m13 = t13;
	}

	/**
	 * Translate the source matrix and stash the result in the destination matrix
	 * @param vec The vector to translate by
	 * @return The translated matrix
	 */
	public static void translate(Vector2f vec, Matrix mat) {
		if (mat == null)
			mat = new Matrix();
		Matrix src = new Matrix();
		Matrix.load(mat, src);
		
		mat.m30 += src.m00 * vec.x + src.m10 * vec.y;
		mat.m31 += src.m01 * vec.x + src.m11 * vec.y;
		mat.m32 += src.m02 * vec.x + src.m12 * vec.y;
		mat.m33 += src.m03 * vec.x + src.m13 * vec.y;
	}

	public static void translate(Vector3f vec, Matrix mat) {
		if (mat == null)
			mat = new Matrix();
		Matrix src = new Matrix();
		Matrix.load(mat, src);

		mat.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
		mat.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
		mat.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
		mat.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
	}

	/**
	 * Transpose this matrix and place the result in another matrix
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the transposed matrix
	 */
	public Matrix transpose(Matrix dest) {
		return transpose(this, dest);
	}

	/**
	 * Transpose the source matrix and place the result in the destination matrix
	 * @param src The source matrix
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the transposed matrix
	 */
	public static Matrix transpose(Matrix src, Matrix dest) {
		if (dest == null)
		   dest = new Matrix();
		float m00 = src.m00;
		float m01 = src.m10;
		float m02 = src.m20;
		float m03 = src.m30;
		float m10 = src.m01;
		float m11 = src.m11;
		float m12 = src.m21;
		float m13 = src.m31;
		float m20 = src.m02;
		float m21 = src.m12;
		float m22 = src.m22;
		float m23 = src.m32;
		float m30 = src.m03;
		float m31 = src.m13;
		float m32 = src.m23;
		float m33 = src.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	/**
	 * @return the determinant of the matrix
	 */
	public float determinant() {
		float f =
			m00
				* ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
					- m13 * m22 * m31
					- m11 * m23 * m32
					- m12 * m21 * m33);
		f -= m01
			* ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
				- m13 * m22 * m30
				- m10 * m23 * m32
				- m12 * m20 * m33);
		f += m02
			* ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
				- m13 * m21 * m30
				- m10 * m23 * m31
				- m11 * m20 * m33);
		f -= m03
			* ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
				- m12 * m21 * m30
				- m10 * m22 * m31
				- m11 * m20 * m32);
		return f;
	}

	/**
	 * Calculate the determinant of a 3x3 matrix
	 * @return result
	 */

	private static float determinant3x3(float t00, float t01, float t02,
					 float t10, float t11, float t12,
					 float t20, float t21, float t22)
	{
		return   t00 * (t11 * t22 - t12 * t21)
			   + t01 * (t12 * t20 - t10 * t22)
			   + t02 * (t10 * t21 - t11 * t20);
	}

	/**
	 * Invert this matrix
	 * @return this if successful, null otherwise
	 */
	public Matrix invert() {
		return invert(this, this);
	}

	/**
	 * Invert the source matrix and put the result in the destination
	 * @param src The source matrix
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return The inverted matrix if successful, null otherwise
	 */
	public static Matrix invert(Matrix src, Matrix dest) {
		float determinant = src.determinant();

		if (determinant != 0) {
			/*
			 * m00 m01 m02 m03
			 * m10 m11 m12 m13
			 * m20 m21 m22 m23
			 * m30 m31 m32 m33
			 */
			if (dest == null)
				dest = new Matrix();
			float determinant_inv = 1f/determinant;

			// first row
			float t00 =  determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
			float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
			float t02 =  determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
			float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
			// second row
			float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
			float t11 =  determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
			float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
			float t13 =  determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
			// third row
			float t20 =  determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
			float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
			float t22 =  determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
			float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
			// fourth row
			float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
			float t31 =  determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
			float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
			float t33 =  determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);

			// transpose and divide by the determinant
			dest.m00 = t00*determinant_inv;
			dest.m11 = t11*determinant_inv;
			dest.m22 = t22*determinant_inv;
			dest.m33 = t33*determinant_inv;
			dest.m01 = t10*determinant_inv;
			dest.m10 = t01*determinant_inv;
			dest.m20 = t02*determinant_inv;
			dest.m02 = t20*determinant_inv;
			dest.m12 = t21*determinant_inv;
			dest.m21 = t12*determinant_inv;
			dest.m03 = t30*determinant_inv;
			dest.m30 = t03*determinant_inv;
			dest.m13 = t31*determinant_inv;
			dest.m31 = t13*determinant_inv;
			dest.m32 = t23*determinant_inv;
			dest.m23 = t32*determinant_inv;
			return dest;
		} else
			return null;
	}

	/**
	 * Negate this matrix
	 * @return this
	 */
	public Matrix negate() {
		return negate(this);
	}

	/**
	 * Negate this matrix and place the result in a destination matrix.
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return the negated matrix
	 */
	public Matrix negate(Matrix dest) {
		return negate(this, dest);
	}

	/**
	 * Negate this matrix and place the result in a destination matrix.
	 * @param src The source matrix
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return The negated matrix
	 */
	public static Matrix negate(Matrix src, Matrix dest) {
		if (dest == null)
			dest = new Matrix();

		dest.m00 = -src.m00;
		dest.m01 = -src.m01;
		dest.m02 = -src.m02;
		dest.m03 = -src.m03;
		dest.m10 = -src.m10;
		dest.m11 = -src.m11;
		dest.m12 = -src.m12;
		dest.m13 = -src.m13;
		dest.m20 = -src.m20;
		dest.m21 = -src.m21;
		dest.m22 = -src.m22;
		dest.m23 = -src.m23;
		dest.m30 = -src.m30;
		dest.m31 = -src.m31;
		dest.m32 = -src.m32;
		dest.m33 = -src.m33;

		return dest;
	}
}
