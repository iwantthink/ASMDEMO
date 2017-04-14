package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMReadDemo {
	public static void main(String[] args) {

		try {
			FileInputStream fis = new FileInputStream("Hello1.class");
			ClassReader reader = new ClassReader(fis);
//			ClassReader reader = new ClassReader("test.Hello");
			ClassWriter writer = new ClassWriter(reader,
					ClassWriter.COMPUTE_MAXS);
			ChangeVisitor changer = new ChangeVisitor(writer);
			reader.accept(changer, ClassReader.EXPAND_FRAMES);
			// ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			// writer.visit(52, Opcodes.ACC_PUBLIC, "What", null,
			// "java/lang/Object",
			// null);
			byte[] code = writer.toByteArray();
			System.out.println("code length = " + code.length);
			FileOutputStream fos = new FileOutputStream("Hello1.class");
			fos.write(code);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


class PrintVisitor extends ClassVisitor {

	public PrintVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);

	}

	 @Override
	 public void visit(int version, int access, String name, String signature,
	 String superName, String[] interfaces) {
	 super.visit(version, access, name, signature, superName, interfaces);
	 System.out.println("------visit-------");
	 System.out.println("version = " + version);
	 System.out.println("access = " + access);
	 System.out.println("name = " + name);
	 System.out.println("signature = " + signature);
	 System.out.println("superName = " + superName);
	 System.out.println("interfaces = " + interfaces);
	 System.out.println("------visit end-------\n");
	 }

	 @Override
	 public FieldVisitor visitField(int access, String name, String desc,
	 String signature, Object value) {
	 System.out.println("------visitField-------");
	 System.out.println("access = " + access);
	 System.out.println("name = " + name);
	 System.out.println("signature = " + signature);
	 System.out.println("desc = " + desc);
	 System.out.println("value = " + value);
	 System.out.println("------visitField end-------\n");
	 return super.visitField(access, name, desc, signature, value);
	 }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		System.out.println("------visitMethod-------");
		System.out.println("access = " + access);
		System.out.println("name = " + name);
		System.out.println("signature = " + signature);
		System.out.println("desc = " + desc);
		System.out.println("exceptions = " + exceptions);
		System.out.println("------visitMethod end-------\n");
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		System.out.print("--------end--------");
	}
}