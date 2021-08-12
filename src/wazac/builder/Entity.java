package main.wewaz.auth.bean.builder;


import java.sql.Statement;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.config.WewazDbState;

public class Entity<T> {
	
	T model;
	EntityBuilder<T> builder;

	public Entity(EntityBuilder<T> pbuilder) {
		super();
		this.model = (T) pbuilder.getModel();
	}
	
	public T getModel() {
		return model;
	}
	
	public void setModel(T model) {
		this.model = model;
	}
	
	public EntityBuilder<T> getBuilder(PreparedStatement ps) throws SecurityException, SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		EntityBuilder<T> builder = new EntityBuilder();
		builder.setModel(ps, model, null);
		return builder;
	}

        public void setBuilder(EntityBuilder<T> builder) {
            this.builder = builder;
        }
        
	@Override
	public String toString() {
		return "Entity [model=" + model + "]";
	}
	
	public static class EntityBuilder<T> implements WewazDbState<T> {
		
		private Object model;
		
		public EntityBuilder() {
		}

		public EntityBuilder(T model) {
			super();
			this.model = model;
		}
		
		public T getModel() {
			return (T) model;
		}
		
		public void setModel(PreparedStatement ps, Object clazz, Object property) throws SecurityException, SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			if(clazz != null && property == null) {
				for (int i = 0; i < clazz.getClass().getDeclaredFields().length; i++) {
					
					Field f = clazz.getClass().getDeclaredFields()[i];
					
					f.setAccessible(true);
					String fValue = (String) f.get(clazz);
					
					if(ps != null) {
						this.sqlOnStateChange(ps, i+1, fValue);
					}
				}
			} 
			
			/*if (clazz != null && property != null) {
				
			} */
			this.model = clazz;
		}

		@Override
		public void sqlOnStateChange(PreparedStatement ps, int column, String propertyValue) throws SQLException {
			ps.setString(column, propertyValue);
		}

		@Override
		public void onCreate(PreparedStatement ps, WewazDbState db, Object o) throws SecurityException, IllegalArgumentException, IllegalAccessException, SQLException, InvocationTargetException {
			
			if(db != null) {
				ps.executeUpdate();
				ps.close();
			} else {
				setModel(ps, db, null);
			}
			
		}

		@Override
		public void onReadOne(PreparedStatement ps, WewazDbState db, String param) throws SecurityException, IllegalArgumentException, IllegalAccessException, SQLException, InvocationTargetException, InstantiationException, NoSuchMethodException {
			for (int i = 0; i < db.getModel().getClass().getDeclaredFields().length; i++) {
				try {
					if(db.getModel().getClass().getDeclaredMethods()[i].getName().toLowerCase().contains("Id".toLowerCase())
							&& db.getModel().getClass().getDeclaredMethods()[i].getName().contains("get")) {
						
						this.sqlOnStateChange(ps, 1, param);
						System.out.println(db.getModel());
						sqlResultSet(ps, null, db);
						
					}
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException
						| InvocationTargetException | SQLException e) {
					e.printStackTrace();
				}
			}
			ps.close();
		}

		@Override
		public void onRead(Statement stmt, ResultSet rs, WewazDbState db, List<T> list) throws SQLException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
			sqlStatement(stmt, rs, db, list);
			
		}

		@Override
		public void onDelete(PreparedStatement ps, WewazDbState db, String param) throws SecurityException, IllegalArgumentException, IllegalAccessException, SQLException {
			for (int i = 0; i < db.getModel().getClass().getDeclaredFields().length; i++) {
				try {
					if(db.getModel().getClass().getDeclaredMethods()[i].getName().toLowerCase().contains("Id")
							&& db.getModel().getClass().getDeclaredMethods()[i].getName().contains("set")) {
						setModel(ps, db, param);
					}
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException
						| InvocationTargetException | SQLException e) {
					e.printStackTrace();
				}
			}
			ps.executeUpdate();
			ps.close();
		}

		@Override
		public void onUpdate(PreparedStatement ps, WewazDbState db, String param) throws SecurityException,
				IllegalArgumentException, IllegalAccessException, SQLException, InvocationTargetException {
			if(db != null) {
				ps.setString(db.getModel().getClass().getDeclaredFields().length+1, param);
				ps.executeUpdate();
				ps.close();
			} else {
				setModel(ps, db, null);
			}
			
		}

		@Override
		public void sqlResultSet(PreparedStatement ps, ResultSet rs, WewazDbState db) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException, SecurityException {
			rs = ps.executeQuery();
			if(rs.next()) {
				Object newObject = db.getModel().getClass().newInstance();
				for (int i = 0; i < db.getModel().getClass().getDeclaredFields().length; i++) {
					Field f = db.getModel().getClass().getDeclaredFields()[i];
					String upper = String.valueOf(f.getName().charAt(0)).toUpperCase();
					String methodName = "set".concat(upper).concat(f.getName().substring(1));
					Method setNameMethod = db.getModel().getClass().getMethod(methodName, String.class);
					f.setAccessible(true);
					setNameMethod.invoke(db.getModel(), rs.getString(f.getName()));
					
				}
			}
		}

		@Override
		public void sqlStatement(Statement stmt, ResultSet rs, WewazDbState db, List<T> list) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, SQLException {
			while(rs.next()) {
				Object newObject = db.getModel().getClass().newInstance();
				for (int i = 0; i < db.getModel().getClass().getDeclaredFields().length; i++) {
					Field f = db.getModel().getClass().getDeclaredFields()[i];
					String upper = String.valueOf(f.getName().charAt(0)).toUpperCase();
					String methodName = "set".concat(upper).concat(f.getName().substring(1));
					Method setNameMethod = db.getModel().getClass().getMethod(methodName, Object.class);
					f.setAccessible(true);
					setNameMethod.invoke(db.getModel(), rs.getString(f.getName()));
				}
				list.add((T) newObject);
			}
			
		}
		
		
	}
	
}
