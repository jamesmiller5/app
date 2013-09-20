/*
To get this to work there needs to be a session id and it needs to correspond
to the me User in each function and figure out how to run cliche commands from code
*/

package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import app.*;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

/**
 * Unit test for {@link app.viewSubjectiveNetwork}.
 */
public class ViewSubjectiveNetwork {
	
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
			User me=new User();

			//Test data
			User u1;
			u1=new User();
					
			new TrustEdge(me,u1,new Subject("testing"));
		
			//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
					User me=new User();

					//Test data
					User u1,u2;
					u1=new User();
					u2=new User();
					
					new TrustEdge(me,u1,new Subject("testing"));
					new TrustEdge(u1,u2,new Subject("testing"));
		
					//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
					User me=new User();

					//Test data
					User u1,u2;
					u1=new User();
					u2=new User();
					
					new TrustEdge(me,u1,new Subject("testing"));
		
					//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
					User me=new User();

					//Test data
					User u1;
					u1=new User();
					
					new TrustEdge(u1,me,new Subject("testing"));
		
					//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
			User me=new User();

			//Test data
			User u1,u2;
			u1=new User();
			u2=new User();
					
			new TrustEdge(me,u2,new Subject("testing"));
			new TrustEdge(u1,me,new Subject("testing"));
		
			//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
				User me=new User();

				//Test data
				User u1;
				u1=new User();
					
				new TrustEdge(me,u1,new Subject("testing"));
		
				//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
				User me=new User();

				//Test data
				User u1,u2;
				u1=new User();
				u2=new User();
					
				new TrustEdge(me,u1,new Subject("testing"));
				new TrustEdge(u1,u2,new Subject("coding"));
		
				//viewSubjectiveNetwork(SESSIONID,"testing",1);
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
				User me=new User();

				//Test data
				User u1,u2,u3;
				u1=new User();
				u2=new User();
				u3=new User();
					
				new TrustEdge(me,u1,new Subject("testing"));
				new TrustEdge(u1,u2,new Subject("coding"));
				new TrustEdge(u2,u3,new Subject("testing"));
		
				//viewSubjectiveNetwork(SESSIONID,"testing",1);
			}
		}
	 
}
