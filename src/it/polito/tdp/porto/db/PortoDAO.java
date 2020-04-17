package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.CoAutoraggio;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				conn.close();
				return autore;
			}
			conn.close();
			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public HashSet<Author> getAllAutori() {

		final String sql = "SELECT * FROM author ORDER BY lastname, firstname ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			HashSet<Author> result= new HashSet<Author>();
			while (rs.next()) {
				
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(autore);				
			}
			conn.close();

			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				conn.close();

				return paper;
			}
			conn.close();

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public ArrayList<CoAutoraggio> getAutoraggio() {

		final String sql = "SELECT c.authorid AS autore1, c2.authorid AS autore2 " + 
				"FROM creator c, creator c2 " + 
				"WHERE c.eprintid=c2.eprintid AND c.authorid!=c2.authorid ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			
			ArrayList<CoAutoraggio> result= new ArrayList<CoAutoraggio>();
			while (rs.next()) {
				CoAutoraggio au=new CoAutoraggio(rs.getInt("autore1"),rs.getInt("autore2"));
				result.add(au);
			}
			conn.close();

			return result;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper getPaperComune(Author a1, Author a2) {

		final String sql = "SELECT c.eprintid, title, issn, publication, TYPE, types " + 
				"FROM creator c, creator c2, paper p " + 
				"WHERE c.eprintid=c2.eprintid and c.authorid=? AND c2.authorid=? AND p.eprintid=c.eprintid ";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());
			ResultSet rs = st.executeQuery();
			
			rs.next();
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
			conn.close();

			return paper;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
}