package com.cronoteSys.model.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.cronoteSys.model.vo.LoginVO;
import com.cronoteSys.model.vo.UserVO;

public class LoginDAO extends GenericsDAO<LoginVO, Integer> {

	public LoginDAO() {
		super(LoginVO.class);
	}

	public String getEmailFromUser(Integer userId) {
		return entityManager.createQuery("Select l.email from LoginVO l where l.tbUser.id=" + userId).getResultList()
				.get(0).toString();
	}

	public int changePassword(String email, String password) {
		LoginVO l = (LoginVO) entityManager.createQuery("From LoginVO where email=:email", LoginVO.class)
				.setParameter("email", email).getSingleResult();
		l.setPasswd(password);
		entityManager.merge(l);
		return l.getIdLogin() != null ? 1 : 0;
	}

	public List<LoginVO> listAll() {
		try {
			List<LoginVO> login;
			// uma
			// sessao
			login = entityManager.createNativeQuery("SELECT * FROM tb_login", LoginVO.class).getResultList();
			if (login.size() > 0) {
				return login;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public UserVO verifiedUser(String email, String pass) {
		try {
			List<UserVO> user = entityManager.createQuery("from UserVO u where login.email = :email", UserVO.class)
					.setParameter("email", email).getResultList();
			if (user.size() > 0) {
				if (user.get(0).getLogin().getPasswd().equals(pass)) {

					return user.get(0);
				}
			}

		} catch (Exception e) {
			System.out.println("Erro de verifica��o de usuario: " + e.getMessage());
		}
		return null;
	}

	public Long loginExists(String sEmail) {
		try {
			return entityManager.createQuery("SELECT count(l) FROM LoginVO l WHERE email =:email", Long.class)
					.setParameter("email", sEmail).getSingleResult();

		} catch (Exception e) {
			System.out.println("Erro na verificação de email: " + e.getMessage());
		}
		return 0L;
	}

	public LoginVO loginByUser(UserVO u) {
		try {
			List<LoginVO> login = entityManager.createQuery("from LoginVO where tbUser=:user").setParameter("user", u)
					.getResultList();
			if (login.size() > 0) {
				return login.get(0);
			}

		} catch (Exception e) {
			System.out.println("Erro na verifica��o de Usuario: " + e.getMessage());
		}
		return null;
	}
}
