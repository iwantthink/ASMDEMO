package aop;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.util.CheckClassAdapter;

public class InsertCodeTest {

	public static void main(String[] args) throws Exception {
		// byte[] ary = createClazz();
		// // 一个Java类解析器，使{@link ClassVisitor}访问现有的类。
		// // 此类解析符合Java类文件格式的字节数组，并为遇到的每个字段，方法和字节码指令调用给定类访问者的适当访问方法。
		// ClassReader classReader = new ClassReader(ary);
		// // 一个以字节码形式生成类的{@link ClassVisitor}。
		// // 更准确地说，这个访问者生成符合Java类文件格式的字节数组。
		// // 它可以单独使用，从头开始生成Java类，
		// // 或者使用一个或多个{@link ClassReader ClassReader}和适配器类访问器从一个
		// // 或多个现有Java类生成一个修改的类。
		// ClassWriter classWriter = new ClassWriter(classReader,
		// ClassWriter.COMPUTE_MAXS);
		// ChangeVisitor changeVisitor = new ChangeVisitor(classWriter);
		// classReader.accept(changeVisitor, ClassReader.EXPAND_FRAMES);
		// try {
		// OutputStream outputStream = new FileOutputStream("Person2.class");
		// outputStream.write(classWriter.toByteArray());
		// outputStream.close();
		// } catch (Exception e) {
		// }
		// System.out.println("Redefine Done!");
		//
		// Class clazz = new MyClassLoader().defineClass(
		// "com.ledboot.ASMTest.Person", classWriter.toByteArray());
		// Field field = clazz.getDeclaredField("name");
		// System.out.println("field.getName(); = " + field.getName());
		// Type type = Type.getType(clazz);
		// String internalName = type.getInternalName();
		// System.out.println("internalName = " + internalName);

		createClazz2();
		
		

	}

	public static byte[] createClazz() {
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC,
				"com/ledboot/ASMTest/Person", null, "java/lang/Object", null);
		cw.visitField(Opcodes.ACC_PRIVATE, "name", "Ljava/lang/String;", null,
				null).visitEnd();
		cw.visitField(Opcodes.ACC_PRIVATE, "age", "I", null, 0).visitEnd();

		MethodVisitor mvConstruct = cw.visitMethod(Opcodes.ACC_PUBLIC,
				"<init>", "()V", null, null);
		mvConstruct.visitVarInsn(Opcodes.ALOAD, 0);
		mvConstruct.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
				"<init>", "()V", false);
		mvConstruct.visitInsn(Opcodes.RETURN);
		mvConstruct.visitMaxs(1, 1);
		mvConstruct.visitEnd();

		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PRIVATE, "showInfo",
				"()V", null, null);
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder",
				"<init>", "()V", false);
		mv.visitLdcInsn("name=");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder",
				"append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
				false);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "com/ledboot/ASMTest/Person",
				"name", "Ljava/lang/String;");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder",
				"append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
				false);
		mv.visitLdcInsn(",age=");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder",
				"append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
				false);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "com/ledboot/ASMTest/Person",
				"age", "I");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder",
				"append", "(I)Ljava/lang/StringBuilder;", false);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder",
				"toString", "()Ljava/lang/String;", false);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(Ljava/lang/String;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(4, 4);
		mv.visitEnd();

		MethodVisitor mvGetName = cw.visitMethod(Opcodes.ACC_PUBLIC, "getName",
				"()Ljava/lang/String;", null, null);
		mvGetName.visitVarInsn(Opcodes.ALOAD, 0);
		mvGetName.visitFieldInsn(Opcodes.GETFIELD,
				"com/ledboot/ASMTest/Person", "name", "Ljava/lang/String;");
		mvGetName.visitInsn(Opcodes.ARETURN);
		mvGetName.visitMaxs(2, 2);
		mvGetName.visitEnd();

		MethodVisitor mvSetName = cw.visitMethod(Opcodes.ACC_PUBLIC, "setName",
				"(Ljava/lang/String;)V", null, null);
		mvSetName.visitVarInsn(Opcodes.ALOAD, 0);
		mvSetName.visitVarInsn(Opcodes.ALOAD, 1);
		mvSetName.visitFieldInsn(Opcodes.PUTFIELD,
				"com/ledboot/ASMTest/Person", "name", "Ljava/lang/String;");
		mvSetName.visitInsn(Opcodes.RETURN);
		mvSetName.visitMaxs(2, 2);
		mvSetName.visitEnd();

		MethodVisitor mvGetAge = cw.visitMethod(Opcodes.ACC_PUBLIC, "getAge",
				"()I", null, null);
		mvGetAge.visitVarInsn(Opcodes.ALOAD, 0);
		mvGetAge.visitFieldInsn(Opcodes.GETFIELD, "com/ledboot/ASMTest/Person",
				"age", "I");
		mvGetAge.visitInsn(Opcodes.IRETURN);
		mvGetAge.visitMaxs(2, 2);
		mvGetAge.visitEnd();

		MethodVisitor mvSetAge = cw.visitMethod(Opcodes.ACC_PUBLIC, "setAge",
				"(I)V", null, null);
		mvSetAge.visitVarInsn(Opcodes.ALOAD, 0);
		mvSetAge.visitVarInsn(Opcodes.ILOAD, 1);
		mvSetAge.visitFieldInsn(Opcodes.PUTFIELD, "com/ledboot/ASMTest/Person",
				"age", "I");
		mvSetAge.visitInsn(Opcodes.RETURN);
		mvSetAge.visitMaxs(2, 2);
		mvSetAge.visitEnd();

		cw.visitEnd();

		try {
			FileOutputStream out = new FileOutputStream("Person.class");
			out.write(cw.toByteArray());
			out.close();
		} catch (Exception e) {
		}
		System.out.println("done!");
		return cw.toByteArray();
	}

	public static void createClazz2() {

		ClassWriter writer = new ClassWriter(Opcodes.ASM5);
		writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/mrb/www/Person",
				null, "java/lang/Object", null);
		writer.visitField(Opcodes.ACC_PRIVATE, "name", "Ljava/lang/String;",
				null, null).visitEnd();
		

		byte[] arg = writer.toByteArray();
		try {
			OutputStream outputStream = new FileOutputStream("Person3.class");
			outputStream.write(writer.toByteArray());
			outputStream.close();
		} catch (Exception e) {
		}

		System.out.println("write person3 is done");

	}

	static class ChangeVisitor extends ClassVisitor {

		public ChangeVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			MethodVisitor mv = super.visitMethod(access, name, desc, signature,
					exceptions);
			if (name.equals("showInfo")) {
				System.out.println("mv =" + mv.getClass());
				System.out.println("access = " + access);
				System.out.println("name = " + name);
				System.out.println("desc = " + desc);
				System.out.println("signature = " + signature);
				System.out.println("exceptions = " + exceptions);
				return new RedefineAdvice(Opcodes.ASM5, mv, access, name, desc);
			}
			return mv;
		}
	}

	static class RedefineAdvice extends AdviceAdapter {

		protected RedefineAdvice(int api, MethodVisitor mv, int access,
				String name, String desc) {
			super(api, mv, access, name, desc);
		}

		@Override
		protected void onMethodEnter() {
			mv.visitCode();
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
					"currentTimeMillis", "()J", false);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
					"println", "(J)V", false);
			super.onMethodEnter();
		}

	}
}
