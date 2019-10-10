package com.cronoteSys.model.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.hibernate.HibernateException;

import com.cronoteSys.model.vo.TeamUser;
import com.cronoteSys.model.vo.TeamVO;
import com.cronoteSys.model.vo.relation.side.TeamMember;

public class TeamDAO extends GenericsDAO<TeamVO, Long> {

	public TeamDAO() {
		super(TeamVO.class);
	}

	public TeamVO saveOrUpdate(TeamVO team) {
		TeamVO savedTeam = super.saveOrUpdate(team);
		savedTeam.setMembers(team.getMembers());
		savedTeam.switchMembersBetweenLists(true);
		savedTeam = super.saveOrUpdate(savedTeam);
		return savedTeam;

	}

	public List<TeamVO> listAll() {
		try {
			List<TeamVO> teams;
			teams = entityManager.createQuery("Select t from TeamVO t", TeamVO.class).getResultList();

			if (teams.size() > 0) {
				for (TeamVO teamVO : teams) {
					teamVO.getTeamUser().removeIf(new Predicate<TeamUser>() {

						@Override
						public boolean test(TeamUser t) {
							// TODO Auto-generated method stub
							return t.getExpiresAt() != null && t.getExpiresAt().isBefore(LocalDateTime.now());
						}
					});
				}
				return teams;
			}

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return null;
	}

	public List<TeamVO> listByUserOwnerOrMember(int userId) {
		List<TeamVO> teams = new ArrayList<TeamVO>();
		try {

			teams = entityManager.createNativeQuery("Select distinct t.id,t.name,t.description,t.id_user,t.teamColor"
					+ " from tb_team t" + " left join teamuser tu on t.id = tu.team " + " where t.id_user =" + userId
					+ " or tu.member in(" + userId + ")", TeamVO.class).getResultList();

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return teams;
	}

	public String getTeamName(int id) {
		String team = entityManager.createNativeQuery("SELECT name	FROM public.tb_team WHERE id=" + id + ";")
				.getResultList().get(0).toString();
		if (team.isEmpty()) {
			return null;
		}
		return team;
	}

	public boolean inviteAccepted2(int member, int team) {
		try {
			TeamVO t = entityManager.createQuery("select t from TeamVO t where t.id =" + team, TeamVO.class)
					.getSingleResult();
			TeamUser expiredInvite = null;
			System.out.println("its null");
			for (TeamUser tu : t.getTeamUser()) {
				System.out.println("for");
				if (tu.getMember().getIdUser() == member) {
					System.out.println("member in the list of members ");

					// if expiracao é antes de agora entao expirou
					if (tu.getExpiresAt() != null && tu.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC-03:00")))) {
						
						System.out.println("expires " + tu.getExpiresAt());
						System.out.println("now" + LocalDateTime.now(ZoneId.of("UTC-03:00")));
						System.out.println("invite broken");
						expiredInvite = tu;
						break;
					} else {
						tu.setInviteAccepted(true);
						tu.setExpiresAt(null);
					}
				}
			}
			// remove convite expirado (apenas se teve uma tentativa de aceitar)
			if (expiredInvite != null) {
				t.getTeamUser().remove(expiredInvite);
				TeamMember expiredMember = new TeamMember(expiredInvite.getMember(), expiredInvite.isInviteAccepted(),
						expiredInvite.getExpiresAt());
				t.getMembers().remove(expiredMember);
			}
			System.out.println("antes do save " + expiredInvite);
			t = saveOrUpdate(t);
			System.out.println("depois do save " + expiredInvite);
			return true && (expiredInvite == null);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<TeamVO> searchWithUserOwnerOrMember(String search, Integer idUser) {
		List<TeamVO> teams = new ArrayList<TeamVO>();
		try {

			teams = entityManager.createNativeQuery("Select distinct t.id,t.name,t.description,t.id_user,t.teamColor"
					+ " from tb_team t " + " left join teamuser tu on t.id = tu.team "
					+ " where LOWER(t.name) like ? and ( t.id_user =" + idUser + " or tu.member in(" + idUser + "))",
					TeamVO.class).setParameter(1, search.toLowerCase() + "%").getResultList();

		} catch (HibernateException e) {
			System.out.println("Problem on list " + e.getMessage());
		}
		return teams;
	}
}
