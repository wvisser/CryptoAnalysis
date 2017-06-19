package test.constraints;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.Test;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import crypto.analysis.ConstraintSolver;
import crypto.analysis.EnsuredCryptSLPredicate;
import crypto.analysis.ParentPredicate;
import crypto.rules.CryptSLRule;
import crypto.rules.CryptSLRuleReader;
import test.IDEALCrossingTestingFramework;
import typestate.interfaces.ISLConstraint;

public class KeyGeneratorTest {

	protected CryptSLRule getCryptSLFile() {
		return CryptSLRuleReader.readFromFile(new File(IDEALCrossingTestingFramework.RESOURCE_PATH + "KeyGenerator.cryptslbin"));
	}

	@Test
	public void testKeyGenerator1() throws NoSuchAlgorithmException {
		Multimap<String, String> values = HashMultimap.create();
		values.put("alg", "AES");
		values.put("keySize", "128");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				return Lists.newLinkedList();
			}
		});
		Integer failed = 0;
		for(ISLConstraint cons : getCryptSLFile().getConstraints()) {
			if (!cs.evaluate(cons, values)) {
				failed++;
			}
		}
		assertEquals(failed, (Integer)0);
	}
	
	@Test
	public void testKeyGenerator2() throws NoSuchAlgorithmException {
		Multimap<String, String> values = HashMultimap.create();
		values.put("alg", "AES");
		values.put("keySize", "234");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				return Lists.newLinkedList();
			}
		});
		Integer failed = 0;
		for(ISLConstraint cons : getCryptSLFile().getConstraints()) {
			if (!cs.evaluate(cons, values)) {
				failed++;
			}
		}
		assertEquals(failed, (Integer)1);
	}

	
}