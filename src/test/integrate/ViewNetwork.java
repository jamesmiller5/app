package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import app.*;
import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.InputConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

/**
 * Unit test for {@link app.viewTrustNetwork}.
 */
public class ViewNetwork {

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	/*
		checks cases:
		user with valid tree and checks edge directions are correct
		user with no outgoing edges
	*/
	@Test
	public void check_cases() {
		String out="";
		GraphDatabaseService gdb=app.GraphDatabase.get();
                try(Transaction tx=gdb.beginTx()){
					User jason,andrew,nat,jim,christina;
					jason=new User();
					jason.addEmail(new Email("jason@j.com"));
					andrew=new User();
					andrew.addEmail(new Email("andrew@a.com"));
					andrew.addEmail(new Email("andrew2@a.com"));
					jim=new User();
					jim.addEmail(new Email("jim@j.com"));
					christina=new User();
					christina.addEmail(new Email("christina@c.com"));
					nat=new User();
					nat.addEmail(new Email("nat@n.com"));

					app.RelationshipFactory.get().getOrCreateRelationship(jason.getInternalNode(),andrew.getInternalNode(),app.RelType.TO);
					app.RelationshipFactory.get().getOrCreateRelationship(jason.getInternalNode(),jim.getInternalNode(),app.RelType.TO);
					app.RelationshipFactory.get().getOrCreateRelationship(jim.getInternalNode(),christina.getInternalNode(),app.RelType.TO);
					app.RelationshipFactory.get().getOrCreateRelationship(nat.getInternalNode(),jim.getInternalNode(),app.RelType.TO);
					
					for(Path pos:Traversal.description().breadthFirst().evaluator(Evaluators.fromDepth(1)).relationships(RelType.TO,Direction.OUTGOING).traverse(jason.getInternalNode())){
                                Node n=pos.endNode();
                                User u=new User(n);
                                for(Email e: u.viewEmails()){
                                        out=out+e.getAddress()+"\n";
										break;
                                }
                        }
                        
                        assertTrue(out.equals("andrew@a.com\njim@j.com\nchristina@c.com\n"));
                        out="";
                        
						for(Path pos:Traversal.description().breadthFirst().evaluator(Evaluators.fromDepth(1)).relationships(RelType.TO,Direction.OUTGOING).traverse(andrew.getInternalNode())){
                                Node n=pos.endNode();
                                User u=new User(n);
                                for(Email e: u.viewEmails()){
                                        out=out+e.getAddress()+"\n";
										break;
                                }
                        }
                        
                        assertTrue(out.equals("\n"));
                        
        }
        
	}
}
