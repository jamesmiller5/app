/*
To get this to work there needs to be a session id and it needs to correspond
to the me User in each function and figure out how to run cliche commands from code
*/

package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import app.*;
import java.util.LinkedList;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

/**
 * Unit test for {@link app.viewSubjectiveNetwork}.
 */
public class ViewSubjectiveNetwork {
	User me;
	public static final String SESSIONID="valid_session_id";

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	/*
	 * checks the simple case of two users with the same subject
	 * A->B
	 */

	@Test
	public void check_Simple1(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){
			me=new User();

			//Test data
			User u1;
			u1=new User();
			u1.addEmail(new Email("u@u.com"));

			new TrustEdge(me,u1,new Subject("testing"));

			assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",1).equals("u@u.com\n"));
		}
	}

	/*
	 *chcks the case of 3 users with the same subject
	 *USER->B->C
	 */

	@Test
	public void check_Simple2(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
                try(Transaction tx=gdb.beginTx()){
					me=new User();

					//Test data
					User u1,u2;
					u1=new User();
					u2=new User();

					u1.addEmail(new Email("a@a.com"));
					u2.addEmail(new Email("b@b.com"));

					new TrustEdge(me,u1,new Subject("testing"));
					new TrustEdge(u1,u2,new Subject("testing"));

					assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",2).equals("a@a.com\nb@b.com\n"));
				}
	}

	/*
	 * checks the case of 2 connected users and
	 * one loner with the same subject
	 * USER->B
	 * C
	 */

	@Test
	public void check_simple3(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
                try(Transaction tx=gdb.beginTx()){
					me=new User();

					//Test data
					User u1,u2;
					u1=new User();
					u2=new User();

					u1.addEmail(new Email("a@a.com"));

					new TrustEdge(me,u1,new Subject("testing"));

					assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",1).equals("a@a.com\n"));
				}
	}

	/*
	 * checks the case of trust not going backwards with the same subject
	 * A->USER
	 */

	@Test
	public void check_simple4(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
                try(Transaction tx=gdb.beginTx()){
					me=new User();

					//Test data
					User u1;
					u1=new User();

					new TrustEdge(u1,me,new Subject("testing"));

					assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",1).equals(""));
				}
	}

	/*
	 * checks the case of somone trusting
	 * the email that we are viewing with the same subject
	 * A->USER->C
	 */

	@Test
	public void check_simple5(){
		GraphDatabaseService gdb=app.GraphDatabase.get();
        try(Transaction tx=gdb.beginTx()){
			me=new User();

			//Test data
			User u1,u2;
			u1=new User();
			u2=new User();

			u2.addEmail(new Email("a@a.com"));

			new TrustEdge(me,u2,new Subject("testing"));
			new TrustEdge(u1,me,new Subject("testing"));

			assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",1).equals("a@a.com\n"));
		}
	}

	/*
	 * checks case of 2 users trusting each
	 * other on non specified subject
	 * ME-"testing"->u1
	 * subject="planning"
	 */

	 @Test
	 public void check_subject1(){
		 GraphDatabaseService gdb=app.GraphDatabase.get();
		    try(Transaction tx=gdb.beginTx()){
				me=new User();

				//Test data
				User u1;
				u1=new User();

				new TrustEdge(me,u1,new Subject("testing"));

				assertTrue(viewSubjectiveNetwork(SESSIONID,"planning",1).equals(""));
			}
		}

	/*
	 * checks case of 3 users trusting each
	 * other on different subjects
	 * ME-"testing"->u1
	 * u1-"coding"->u2
	 * subject="planning"
	 */

	 @Test
	 public void check_subject2(){
		 GraphDatabaseService gdb=app.GraphDatabase.get();
		    try(Transaction tx=gdb.beginTx()){
				me=new User();

				//Test data
				User u1,u2;
				u1=new User();
				u2=new User();

				u1.addEmail(new Email("a@a.com"));

				new TrustEdge(me,u1,new Subject("testing"));
				new TrustEdge(u1,u2,new Subject("coding"));

				assertTrue(viewSubjectiveNetwork(SESSIONID,"planning",3).equals(""));
			}
		}


	/*
	 * checks case of a middle user breaking
	 * the trust train
	 * ME-"testing"->u1
	 * u1-"coding"->u2
	 * u2-"testing"->u3
	 * subject="planning"
	 */

	 @Test
	 public void check_subject3(){
		 GraphDatabaseService gdb=app.GraphDatabase.get();
		    try(Transaction tx=gdb.beginTx()){
				me=new User();

				//Test data
				User u1,u2,u3;
				u1=new User();
				u2=new User();
				u3=new User();

				u1.addEmail(new Email("a@a.com"));
				u2.addEmail(new Email("b@b.com"));
				u3.addEmail(new Email("c@c.com"));

				new TrustEdge(me,u1,new Subject("testing"));
				new TrustEdge(u1,u2,new Subject("coding"));
				new TrustEdge(u2,u3,new Subject("testing"));

				assertTrue(viewSubjectiveNetwork(SESSIONID,"testing",1).equals("a@a.com\n"));
			}
	 }

	 public String viewSubjectiveNetwork( String session_id, String subject, int threshold ) {
		String out="";
		 GraphDatabaseService gdb=app.GraphDatabase.get();
		 try(Transaction tx=gdb.beginTx()){
			 Node start=me.getInternalNode();
			 LinkedList<Node> q=new LinkedList<Node>();
			 LinkedList<String> mark=new LinkedList<String>();
			 int depth=0;

			 //BFS
			 q.addFirst(start);
			 for(Email e:new User(start).viewEmails()){
				 mark.add(e.getAddress());
				 break;
			 }
			 while(!q.isEmpty()){
				 Node temp;
				 temp=(Node)q.removeLast();
				 if(depth==threshold){
					 return out;
				 }
				 depth++;
				 // r is relationship from User to TE
				 for(Relationship r: temp.getRelationships(RelType.FROM)){
					 //r2 is relationship from TE to next User
					 for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
						 //accessing Email for identification to print
						 for(Email e:new User(r2.getEndNode()).viewEmails()){
							 //if email has hasnt been added and the subject is correct
							 if(!mark.contains(e.getAddress()) && r2.getStartNode().getProperty("subject").equals(subject)){
								 mark.add(e.getAddress());
								 q.addFirst(r2.getEndNode());
								 for(Email e1:new User(r2.getEndNode()).viewEmails()){
									 out=out+e1.getAddress()+"\n";
									 break;
								 }
							 }
						 }
						 break;
					 }
				 }
			 }

		 }
		 System.out.print(out);
		 return out;
	 }
}
