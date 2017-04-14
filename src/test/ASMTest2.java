package test;

import java.io.IOException;
import java.util.zip.CRC32;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMTest2 {
	
	public static void main(String[] args) throws IOException{
		   ClassReader cr = new ClassReader("test.ASMTest1");
	       ClassWriter cw = new ClassWriter(cr, 0);
	       ClassVisitor cv = new ClassAdapter(Opcodes.ASM5,cw);
	       cr.accept(cv, 0);
	       byte[] b = cw.toByteArray();
	      
	}

}

class ClassAdapter extends ClassVisitor{

	public ClassAdapter(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		if ("setName".equals(name)) {
			System.out.println("hello");
			return null;
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
}
