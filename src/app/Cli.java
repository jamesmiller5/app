package app;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.GraphDatabaseService;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import asg.cliche.InputConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

public class Cli {

	private HashMap<String, Session> session_table = new HashMap<String, Session>();

	private class Result {
		boolean success = false;
		Session session = null;
		Email email = null;
		String payload = "";

		public Result( boolean ans, String reason ) {
			success = ans;
			payload = reason;
		}

		public Result( boolean ans, Session s ) {
			success = ans;
			session = s;
		}

		public Result( boolean ans, Email e ) {
			success = ans;
			email = e;
		}

		public String toString() {
			if( success )
				return "success:" + payload;
			else
				return "failure:" + payload;
		}
	}

	private Result validateSession( String session_id ) {
		if( !session_table.containsKey( session_id ) )
			return new Result(false, "Invalid Session");

		Session s = session_table.get(session_id);

		return new Result(true, s);
	}

	private Result validateEmail( String address, boolean mustExist ) {
		Email email = null;

		if( !Email.isValidAddress(address) ) {
			return new Result(false, "Invalid email");
		}

		if( address.contains("gmail") ) {
			return new Result(false, "No gmail! The NSA is watching!");
		}

		if( address.contains("bob") ) {
			return new Result(false, "Crazy bob rejects this email!");
		}

		try(Transaction tx = GraphDatabase.get().beginTx()) {
			if( mustExist ) {
				//check email is valid
				Node n = Entity.findExistingNode(LabelDef.EMAIL, Email.EMAIL_KEY, address);

				if( n == null ) {
					tx.failure();
					return new Result(false, "Invalid email");
				}
				email = new Email( n );
			} else {
				try {
					email = new Email(address);
				} catch(IllegalArgumentException e) {
					tx.failure();
					return new Result(false, "Invalid email");
				}
			}

			tx.success();
			return new Result(true, email);
		}
	}

	@Command
	public Result login(
			@Param(name="email address") String email,
			@Param(name="password") String pass ) {
		Session session = Session.createFromLogin(email, pass);
		if( session == null )
			return new Result(false, "Invalid Login");

		String session_id = "";
		for( int i=0; i < 20; i++ ) {
			session_id = randomString(4);
			if( session_table.containsKey( session_id ) ) {
				continue;
			}

			session_table.put( session_id, session );
		}

		return new Result(true, "Session:"+session_id);
	}

	@Command
	public Result logout( @Param(name="session id") String session_id ) {
		Result res = validateSession( session_id );
		if( !res.success )
			return res;

		session_table.remove(session_id);

		return res;
	}

	@Command
	public Result signup( @Param(name="email address") String address ) {
		Result res = validateEmail(address, false);

		Email email = res.email;
		return (new Result(true, "ClaimToken:" + email.getClaimToken().toString()));
	}

	@Command
	public Result register(
			@Param(name="claim token", description="claim token for email address") String ct,
			@Param(name="password") String pass,
			@Param(name="password verify") String passVer ) {
		try(Transaction tx = GraphDatabase.get().beginTx()) {
		
			String password = null;
			
			if(pass.toLowerCase().equals(passVer.toLowerCase())) {
				if(pass.toLowerCase().contains("andrew")) {
					pass = passVer = pass.replaceAll("andrew", "mack");
				}else if(pass.toLowerCase().contains("jim") || pass.toLowerCase().contains("james")) {
					pass = passVer = pass.replaceAll("jim", "miller").replaceAll("james", "miller");
				}else if(pass.toLowerCase().contains("jason")) {
					pass = passVer = pass.replaceAll("jason", "salter");
				}else if(pass.toLowerCase().contains("mitch") || pass.toLowerCase().contains("mitchell")) {
					pass = passVer = pass.replaceAll("mitchell", "mounts");
				}else if(pass.toLowerCase().contains("nathan") || pass.toLowerCase().contains("nathaniel")) {
					pass = passVer = pass.replaceAll("nathan", "cherry").replaceAll("nathaniel", "cherry");
				}else if(pass.toLowerCase().contains("christina")) {
					pass = passVer = pass.replaceAll("christina", "atallah";
				}
			}
			
			if(pass.toLowerCase().contains(passVer.toLowerCase())) {
				password = passVer;
			}else if(passVer.toLowerCase().contains(pass.toLowerCase())) {
				password = pass;
			}else {
				return (new Result(false, "bad password"));
			}

			Email e = new Email(Entity.findExistingNode(LabelDef.EMAIL, Email.CLAIM_TOKEN, ct));

			User u = new User();
			u.setPassword(password.toLowerCase());
			u.addEmail(e);

			tx.success();
			return (new Result(true, "registration complete"));
		}
	}

	@Command
	public Result addEmail(
			@Param(name="session id") String session_id,
			@Param(name="email address") String address,
			@Param(name="claim token") String ct) {
		Result res = validateSession( session_id );
		Session session = res.session;

		res = validateEmail(address, false);
		Email email = res.email;

		session.user.addEmail(email);

		return new Result(true, "Email Added");
	}

	@Command
	public Result deleteEmail(
			@Param(name="session id") String session_id,
			@Param(name="email address") String address ) {
		Result res = validateSession( session_id );
		Session session = res.session;

		//email must exist as claimtoken should have been added
		res = validateEmail(address, true);
		if( !res.success ) {
			return res;
		}
		Email email = res.email;

		try {
			if( address.contains("uk") )
			  return new Result(false, "UK emails a permanent!");

			session.user.removeEmail(email);
		} catch(IllegalStateException e) {
			return new Result(false, "Invalid email to remove");
		}

		return new Result(false, "Removed email");
	}

	@Command
	public Result addToPortfolio(
			@Param(name="session id") String session_id,
			@Param(name="description") String description,
			@Param(name="resource") String resource ) {
		Result res = validateSession( session_id );
		if( !res.success )
			return res;
		Session session = res.session;

		Citation c = new Citation(description, resource);

		session.user.addToPortfolio(c);

		return new Result(true, "Citation Added");
	}

	@Command
	public Result removeFromPortfolio(
			@Param(name="session id") String session_id,
			@Param(name="citation token") String cit ) {
		try(Transaction tx = GraphDatabase.get().beginTx()) {
			Result res = validateSession( session_id );
			if( !res.success ){
				return res;
			}

			try {
				Citation c = new Citation(new Token(cit));
				if (c == null) {
					return new Result(false, "Invalid session");
				}
				c.delete();
				tx.success();
				return new Result(true, "Citation removed");
			}

			catch(IllegalArgumentException e ) {
				return new Result(false, "Bad Citation ID");
			}
		}
	}

	@Command
	public Result viewPortfolio( @Param(name="email address") String address ) {
		//find Email for Email. call email.getUser() to get User.
		//then call user.viewPortfolio which returns an iterator
		//over the citations, all of which i want to print

		//email must exist as claimtoken should have been added
		try(Transaction tx = GraphDatabase.get().beginTx()) {
			Result res = validateEmail(address, true);
			if( !res.success ) {
				return res;
			}

			Email email = res.email;

			if (email.getUser() == null) {
				//not found
				return new Result(false, "Invalid email, no profile associated");
			}
			User user = email.getUser();
			Iterator<Citation> iterator = user.viewPortfolio().iterator();
			StringBuilder output = new StringBuilder();
			while (iterator.hasNext()) {
				Citation c = iterator.next();
				output.append(c.toString());
			}


			tx.success();
			return new Result(true , output.toString());
		}
	}

	@Command					//shouldn't this have an entity arg?
	public Result trust(
			@Param(name="session id") String session_id,
			@Param(name="address") String address,
			@Param(name="subject") String subject,
			@Param(name="description") String citation_desc,
			@Param(name="resource") String citation_resource ) {
		Result res = validateSession( session_id );
		if( !res.success ){
			return res;
		}
		Session session = res.session;

		res = validateEmail( address, false );
		if( !res.success ){
			return res;
		}

		try(Transaction tx=GraphDatabase.get().beginTx()){
			Email email = res.email;
			Citation c;
			Subject s;
			TrustEdge te;

			try {
				c = new Citation(citation_desc, citation_resource);
			} catch( IllegalArgumentException e ) {
				return new Result(false, "Invalid citation strings");
			}

			try {
				s = new Subject(subject);
			} catch( IllegalArgumentException e ) {
				return new Result(false, "Invalid subject");
			}

			try {
				te = new TrustEdge(session.user, email, s );
			} catch( IllegalArgumentException e ) {
				return new Result(false, "Internal error");
			}

			te.addCitation( c );

			tx.success();
			return new Result(true, "" + te );
		}
	}

	@Command
	public Result untrust(
			@Param(name="session id") String session_id,
			@Param(name="trust edge") String trustEdge ) {
		Result res = validateSession( session_id );
		if( !res.success ){
			return res;
		}

		try(Transaction tx=GraphDatabase.get().beginTx()){
			//something involved with removing a TrustEdge

			TrustEdge te;
			try {
				te = new TrustEdge( Entity.nodeByID( new Token( trustEdge ) ) );
			} catch( Exception e ) {
				return new Result(false, "Invalid TrustEdge");
			}

			te.delete();
			tx.success();
		}

		return new Result(true,"");
	}

	/*
	 * prints all users who are trusted by the logged in user
	 * in the subject passed to the function
	 */

	@Command
	public Result viewSubjectiveNetwork(
			@Param(name="session id") String session_id,
			@Param(name="subject") String subject,
			@Param(name="threshold") int threshold ) {
		try(Transaction tx=GraphDatabase.get().beginTx()){
			Session s=session_table.get(session_id);
			User me=s.user;
			Node start=me.getInternalNode();
			LinkedList<Node> q = new LinkedList<Node>();
			LinkedList<String> mark = new LinkedList<String>();
			int depth=0;

			//BFS
			q.addFirst(start);
			for(Email e:new User(start).viewEmails()){
				mark.add(e.getAddress());
			}
			while(!q.isEmpty()){
				Node temp;
				temp=q.removeLast();
				if(depth==threshold-1){
					return new Result(true,"");
				}
				depth++;
				// r is relationship from User to TE
				for(Relationship r: temp.getRelationships()){
					//r2 is relationship from TE to next User
					for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
						//accessing Email for identification to print
						for(Email e:new User(r2.getEndNode()).viewEmails()){
							//if email has hasnt been added and the subject is correct
							if(!mark.contains(e.getAddress())){
								mark.add(e.getAddress());
								q.addLast(r2.getEndNode());
								for(Email e1:new User(r2.getEndNode()).viewEmails()){
									System.out.print(e1.getAddress());
								}
							}
						}

					}
				}
			}
		}
		return new Result(false,"");
	}

	/*
	 * prints all user's primary emails that
	 * the user associated with the argument email
	 * has trusted on any subject
	 */

	@Command
	public Result viewTrustNetwork( @Param(name="email address") String address ) {
		try(Transaction tx=GraphDatabase.get().beginTx()){
			Email e2=new Email(address);
			User me=e2.getUser();

			Node start=me.getInternalNode();
			LinkedList<Node> q=new LinkedList<Node>();
			LinkedList<String> mark=new LinkedList<String>();

			//BFS
			q.addFirst(start);
			for(Email e3:new User(start).viewEmails()){
				mark.add(e3.getAddress());
				break;
			}
			while(!q.isEmpty()){
				Node temp;
				temp=(Node)q.removeLast();

				// r is relationship from User to TE
				for(Relationship r: temp.getRelationships()){
					for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
					//	if(r2.getStartNode().hasProperty("Guid")){System.out.println("TrustEdge:"+r2.getStartNode().getProperty("Guid"));}
						System.out.print("subject:" + "subject");
					}
					//r2 is relationship from TE to next User
					for(Relationship r2:r.getEndNode().getRelationships(RelType.TO)){
						//accessing Email for identification to print
						for(Email e:new User(r2.getEndNode()).viewEmails()){
							//if email has hasnt been added
							if(!mark.contains(e.getAddress())){
								mark.add(e.getAddress());
								q.addFirst(r2.getEndNode());
								for(Email e1:new User(r2.getEndNode()).viewEmails()){
									System.out.print(e1.getAddress());
								}
							}
						}
					}
				}
			}
		}
		return new Result(false,"");
	}

	public static void main(String[] args) throws IOException {
		ShellFactory.createConsoleShell("app-sh", "", new Cli()).commandLoop();
	}

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();

	private String randomString( int len ) {
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ )
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();

	}
}
