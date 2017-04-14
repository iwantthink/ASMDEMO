package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class ASMChangeDemo {

	public static void main(String[] args) {

		try {

			FileInputStream fis = new FileInputStream("bin\\test\\Hello.class");
			ClassReader reader = new ClassReader(fis);
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

class ChangeVisitor extends ClassVisitor {

	public ChangeVisitor(ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		System.out.print("--------start--------\n");
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		// MethodVisitor visitor = this.cv.visitMethod(access, name, desc,
		// signature, exceptions);

		MethodVisitor visitor = super.visitMethod(access, name, desc,
				signature, exceptions);

		if (name.equals("main")) {
			return new RedefineAdvice(visitor, access, name, desc);
		}
		return visitor;
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		System.out.print("--------end--------");
	}
}

class RedefineAdvice extends AdviceAdapter {

	protected RedefineAdvice(MethodVisitor mv, int access, String name,
			String desc) {
		super(Opcodes.ASM5, mv, access, name, desc);
	}

	@Override
	protected void onMethodEnter() {
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
				"currentTimeMillis", "()J", false);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
				"println", "(J)V", false);
		super.onMethodEnter();
	}

}
