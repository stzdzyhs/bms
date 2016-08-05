package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.db.bms.entity.Operator;
import com.db.bms.utils.StringUtils;

public class TestStringUtils {

	public static void main(String[] args) {
		String s1,s2;
		
		s1 = ";$delete$'\"\\/#";
		s2 = StringUtils.removeSpecialChars(s1);
		Assert.assertTrue(s2, "delete".equals(s2));
		
		List<Operator> list = new ArrayList<Operator>();
		for(int i=0;i<100;i++) {
			Operator o = new Operator();
			o.setOperatorName("name " + i);
			list.add(o);
		}
		
		System.out.println("ccccccc");
		String c = StringUtils.concatListProperty(list, Operator.class, "operatorName");
		System.out.println(c);
	}

}
