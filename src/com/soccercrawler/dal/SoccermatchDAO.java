
package com.soccercrawler.dal;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.soccercrawler.entities.Soccermatch;
import com.soccercrawler.entities.SoccermatchId_;
import com.soccercrawler.entities.Soccermatch_;
import com.xdev.dal.JPADAO;

/**
 * Home object for domain model class Soccermatch.
 * 
 * @see Soccermatch
 */
public class SoccermatchDAO extends JPADAO<Soccermatch, Long> {
	public SoccermatchDAO() {
		super(Soccermatch.class);
	}
	
	public String searchOverUnderResult(final String mode, final BigDecimal currentTotalLine,
			final BigDecimal openTotalLine, final BigDecimal currentOuOddsTeam1, final BigDecimal openOuOddsTeam1,
			final BigDecimal currentOuOddsTeam2, final BigDecimal openOuOddsTeam2) {

		final List<Long> winList = countingOverUnders((short) 1, mode, currentTotalLine, openTotalLine,
				currentOuOddsTeam1, openOuOddsTeam1, currentOuOddsTeam2, openOuOddsTeam2);
		final List<Long> lossList = countingOverUnders((short) -1, mode, currentTotalLine, openTotalLine,
				currentOuOddsTeam1, openOuOddsTeam1, currentOuOddsTeam2, openOuOddsTeam2);
		final long win = winList.get(0);
		final long loss = lossList.get(0);

		int winPercent = 100;
		if ((win + loss) != 0) {
			winPercent = (int) ((win * 100) / (win + loss));
		} else {
			return " No Result";
		}

		return " Win : " + winPercent + " % (" + win + ")" + "  Loss : " + (100 - winPercent) + " % (" + loss + ")";

	}

	public String searchSpreadOddsResult(final String mode, final BigDecimal currentSpreadTeam1,
			final BigDecimal currentOddsTeam1, final BigDecimal openSpreadTeam1, final BigDecimal openOddsTeam1,
			final BigDecimal currentSpreadTeam2, final BigDecimal currentOddsTeam2, final BigDecimal openSpreadTeam2,
			final BigDecimal openOddsTeam2) {

		final List<Long> winList = countingSpreadOdds((short) 1, mode, currentSpreadTeam1, currentOddsTeam1,
				openSpreadTeam1, openOddsTeam1, currentSpreadTeam2, currentOddsTeam2, openSpreadTeam2, openOddsTeam2);
		final List<Long> lossList = countingSpreadOdds((short) -1, mode, currentSpreadTeam1, currentOddsTeam1,
				openSpreadTeam1, openOddsTeam1, currentSpreadTeam2, currentOddsTeam2, openSpreadTeam2, openOddsTeam2);

		final long win = winList.get(0);
		final long loss = lossList.get(0);

		int winPercent = 100;
		if ((win + loss) != 0) {
			winPercent = (int) ((win * 100) / (win + loss));
		} else {
			return " No Result";
		}

		return " Win : " + winPercent + " % (" + win + ")" + "  Loss : " + (100 - winPercent) + " % (" + loss + ")";
	}

	/**
	 * @queryMethod Do not edit, method is generated by editor!
	 */
	public List<Long> countingSpreadOdds(final Short flag, final String mode, final BigDecimal currentSpreadTeam1,
			final BigDecimal currentOddsTeam1, final BigDecimal openSpreadTeam1, final BigDecimal openOddsTeam1,
			final BigDecimal currentSpreadTeam2, final BigDecimal currentOddsTeam2, final BigDecimal openSpreadTeam2,
			final BigDecimal openOddsTeam2) {
		final EntityManager entityManager = em();

		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		final ParameterExpression<String> modeParameter = criteriaBuilder.parameter(String.class, "mode");
		final ParameterExpression<Short> flagParameter = criteriaBuilder.parameter(Short.class, "flag");
		final ParameterExpression<BigDecimal> currentSpreadTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentSpreadTeam1");
		final ParameterExpression<BigDecimal> currentSpreadTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentSpreadTeam2");
		final ParameterExpression<BigDecimal> currentOddsTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentOddsTeam1");
		final ParameterExpression<BigDecimal> currentOddsTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentOddsTeam2");
		final ParameterExpression<BigDecimal> openSpreadTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openSpreadTeam1");
		final ParameterExpression<BigDecimal> openSpreadTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openSpreadTeam2");
		final ParameterExpression<BigDecimal> openOddsTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openOddsTeam1");
		final ParameterExpression<BigDecimal> openOddsTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openOddsTeam2");

		final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		final Root<Soccermatch> root = criteriaQuery.from(Soccermatch.class);

		criteriaQuery.select(criteriaBuilder.count(root.get(Soccermatch_.id).get(SoccermatchId_.mode)));

		criteriaQuery
				.where(criteriaBuilder.and(
						criteriaBuilder.and(
								criteriaBuilder.and(
										criteriaBuilder.and(
												criteriaBuilder.and(
														criteriaBuilder.and(
																criteriaBuilder.and(
																		criteriaBuilder.and(
																				criteriaBuilder.and(
																						criteriaBuilder.equal(
																								root.get(
																										Soccermatch_.id)
																										.get(SoccermatchId_.mode),
																								modeParameter),
																						criteriaBuilder.equal(
																								root.get(
																										Soccermatch_.spreadBetResult),
																								flagParameter)),
																				criteriaBuilder.or(
																						criteriaBuilder.isNull(
																								currentSpreadTeam1Parameter),
																						criteriaBuilder.equal(
																								root.get(
																										Soccermatch_.currentSpreadTeam1),
																								currentSpreadTeam1Parameter))),
																		criteriaBuilder
																				.or(criteriaBuilder
																						.isNull(currentSpreadTeam2Parameter),
																						criteriaBuilder.equal(
																								root.get(
																										Soccermatch_.currentSpreadTeam2),
																								currentSpreadTeam2Parameter))),
																criteriaBuilder.or(
																		criteriaBuilder
																				.isNull(currentOddsTeam1Parameter),
																		criteriaBuilder.equal(
																				root.get(Soccermatch_.currentOddsTeam1),
																				currentOddsTeam1Parameter))),
														criteriaBuilder
																.or(criteriaBuilder.isNull(currentOddsTeam2Parameter),
																		criteriaBuilder.equal(
																				root.get(Soccermatch_.currentOddsTeam2),
																				currentOddsTeam2Parameter))),
												criteriaBuilder.or(criteriaBuilder.isNull(openSpreadTeam1Parameter),
														criteriaBuilder.equal(root.get(Soccermatch_.openSpreadTeam1),
																openSpreadTeam1Parameter))),
										criteriaBuilder.or(criteriaBuilder.isNull(openSpreadTeam2Parameter),
												criteriaBuilder.equal(root.get(Soccermatch_.openSpreadTeam2),
														openSpreadTeam2Parameter))),
								criteriaBuilder.or(criteriaBuilder.isNull(openOddsTeam1Parameter),
										criteriaBuilder.equal(root.get(Soccermatch_.openOddsTeam1),
												openOddsTeam1Parameter))),
						criteriaBuilder.or(criteriaBuilder.isNull(openOddsTeam2Parameter),
								criteriaBuilder.equal(root.get(Soccermatch_.openOddsTeam2), openOddsTeam2Parameter))));

		final TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(modeParameter, mode);
		query.setParameter(flagParameter, flag);
		query.setParameter(currentSpreadTeam1Parameter, currentSpreadTeam1);
		query.setParameter(currentSpreadTeam2Parameter, currentSpreadTeam2);
		query.setParameter(currentOddsTeam1Parameter, currentOddsTeam1);
		query.setParameter(currentOddsTeam2Parameter, currentOddsTeam2);
		query.setParameter(openSpreadTeam1Parameter, openSpreadTeam1);
		query.setParameter(openSpreadTeam2Parameter, openSpreadTeam2);
		query.setParameter(openOddsTeam1Parameter, openOddsTeam1);
		query.setParameter(openOddsTeam2Parameter, openOddsTeam2);
		return query.getResultList();
	}

	/**
	 * @queryMethod Do not edit, method is generated by editor!
	 */
	public List<Long> countingOverUnders(final Short flag, final String mode, final BigDecimal currentTotalLine,
			final BigDecimal openTotalLine, final BigDecimal currentOuOddsTeam1, final BigDecimal openOuOddsTeam1,
			final BigDecimal currentOuOddsTeam2, final BigDecimal openOuOddsTeam2) {
		final EntityManager entityManager = em();
	
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	
		final ParameterExpression<String> modeParameter = criteriaBuilder.parameter(String.class, "mode");
		final ParameterExpression<Short> flagParameter = criteriaBuilder.parameter(Short.class, "flag");
		final ParameterExpression<BigDecimal> currentTotalLineParameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentTotalLine");
		final ParameterExpression<BigDecimal> openTotalLineParameter = criteriaBuilder.parameter(BigDecimal.class,
				"openTotalLine");
		final ParameterExpression<BigDecimal> currentOuOddsTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentOuOddsTeam1");
		final ParameterExpression<BigDecimal> openOuOddsTeam1Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openOuOddsTeam1");
		final ParameterExpression<BigDecimal> currentOuOddsTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"currentOuOddsTeam2");
		final ParameterExpression<BigDecimal> openOuOddsTeam2Parameter = criteriaBuilder.parameter(BigDecimal.class,
				"openOuOddsTeam2");
	
		final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
	
		final Root<Soccermatch> root = criteriaQuery.from(Soccermatch.class);
	
		criteriaQuery.select(criteriaBuilder.count(root.get(Soccermatch_.id).get(SoccermatchId_.mode)));
	
		criteriaQuery
				.where(criteriaBuilder.and(
						criteriaBuilder.and(
								criteriaBuilder.and(
										criteriaBuilder.and(
												criteriaBuilder.and(
														criteriaBuilder.and(
																criteriaBuilder.and(
																		criteriaBuilder.equal(root.get(Soccermatch_.id)
																				.get(SoccermatchId_.mode), modeParameter),
																		criteriaBuilder.equal(
																				root.get(Soccermatch_.ouBetResult),
																				flagParameter)),
																criteriaBuilder.or(
																		criteriaBuilder.isNull(currentTotalLineParameter),
																		criteriaBuilder.equal(
																				root.get(Soccermatch_.currentTotalLine),
																				currentTotalLineParameter))),
														criteriaBuilder.or(criteriaBuilder.isNull(openTotalLineParameter),
																criteriaBuilder.equal(root.get(Soccermatch_.openTotalLine),
																		openTotalLineParameter))),
												criteriaBuilder.or(criteriaBuilder.isNull(currentOuOddsTeam1Parameter),
														criteriaBuilder.equal(root.get(Soccermatch_.currentOuOddsTeam1),
																currentOuOddsTeam1Parameter))),
										criteriaBuilder.or(criteriaBuilder.isNull(openOuOddsTeam1Parameter),
												criteriaBuilder.equal(root.get(Soccermatch_.openOuOddsTeam1),
														openOuOddsTeam1Parameter))),
								criteriaBuilder.or(criteriaBuilder.isNull(currentOuOddsTeam2Parameter),
										criteriaBuilder.equal(root.get(Soccermatch_.currentOuOddsTeam2),
												currentOuOddsTeam2Parameter))),
						criteriaBuilder.or(criteriaBuilder.isNull(openOuOddsTeam2Parameter),
								criteriaBuilder.equal(root.get(Soccermatch_.openOuOddsTeam2), openOuOddsTeam2Parameter))));
	
		final TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
		query.setParameter(modeParameter, mode);
		query.setParameter(flagParameter, flag);
		query.setParameter(currentTotalLineParameter, currentTotalLine);
		query.setParameter(openTotalLineParameter, openTotalLine);
		query.setParameter(currentOuOddsTeam1Parameter, currentOuOddsTeam1);
		query.setParameter(openOuOddsTeam1Parameter, openOuOddsTeam1);
		query.setParameter(currentOuOddsTeam2Parameter, currentOuOddsTeam2);
		query.setParameter(openOuOddsTeam2Parameter, openOuOddsTeam2);
		return query.getResultList();
	}

	/**
	 * @queryMethod Do not edit, method is generated by editor!
	 */
	public List<Long> countTest() {
		final EntityManager entityManager = em();
	
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	
		final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
	
		final Root<Soccermatch> root = criteriaQuery.from(Soccermatch.class);
	
		criteriaQuery.select(criteriaBuilder.count(root.get(Soccermatch_.id).get(SoccermatchId_.mode)));
	
		final TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}