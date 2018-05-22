package com.soccercrawler.entities;

import com.soccercrawler.dal.SoccermatchDAO;
import com.xdev.dal.DAO;
import com.xdev.util.Caption;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Soccermatch
 */
@DAO(daoClass = SoccermatchDAO.class)
@Caption("{%nameTeam1}")
@Entity
@Table(name = "soccermatch", catalog = "soccerdb")
public class Soccermatch implements java.io.Serializable {

	private SoccermatchId id;
	private String nameTeam1;
	private String nameTeam2;
	private Short spreadBetResult;
	private Short ouBetResult;
	private BigDecimal currentSpreadTeam1;
	private BigDecimal currentOddsTeam1;
	private BigDecimal openSpreadTeam1;
	private BigDecimal openOddsTeam1;
	private BigDecimal currentSpreadTeam2;
	private BigDecimal currentOddsTeam2;
	private BigDecimal openSpreadTeam2;
	private BigDecimal openOddsTeam2;
	private BigDecimal currentTotalLine;
	private BigDecimal openTotalLine;
	private BigDecimal currentOuOddsTeam1;
	private BigDecimal openOuOddsTeam1;
	private BigDecimal currentOuOddsTeam2;
	private BigDecimal openOuOddsTeam2;
	private Short scoreTeam1;
	private Short scoreTeam2;
	private Date createdDate;

	public Soccermatch() {
	}

	@Caption("Id")
	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "ID", nullable = false, columnDefinition = "BIGINT UNSIGNED")),
			@AttributeOverride(name = "mode", column = @Column(name = "MODE", nullable = false, columnDefinition = "VARCHAR", length = 10)) })
	public SoccermatchId getId() {
		return this.id;
	}

	public void setId(SoccermatchId id) {
		this.id = id;
	}

	@Caption("NameTeam1")
	@Column(name = "NAME_TEAM1", columnDefinition = "VARCHAR", length = 45)
	public String getNameTeam1() {
		return this.nameTeam1;
	}

	public void setNameTeam1(String nameTeam1) {
		this.nameTeam1 = nameTeam1;
	}

	@Caption("NameTeam2")
	@Column(name = "NAME_TEAM2", columnDefinition = "VARCHAR", length = 45)
	public String getNameTeam2() {
		return this.nameTeam2;
	}

	public void setNameTeam2(String nameTeam2) {
		this.nameTeam2 = nameTeam2;
	}

	@Caption("SpreadBetResult")
	@Column(name = "SPREAD_BET_RESULT", columnDefinition = "TINYINT")
	public Short getSpreadBetResult() {
		return this.spreadBetResult;
	}

	public void setSpreadBetResult(Short spreadBetResult) {
		this.spreadBetResult = spreadBetResult;
	}

	@Caption("OuBetResult")
	@Column(name = "OU_BET_RESULT", columnDefinition = "TINYINT")
	public Short getOuBetResult() {
		return this.ouBetResult;
	}

	public void setOuBetResult(Short ouBetResult) {
		this.ouBetResult = ouBetResult;
	}

	@Caption("CurrentSpreadTeam1")
	@Column(name = "CURRENT_SPREAD_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentSpreadTeam1() {
		return this.currentSpreadTeam1;
	}

	public void setCurrentSpreadTeam1(BigDecimal currentSpreadTeam1) {
		this.currentSpreadTeam1 = currentSpreadTeam1;
	}

	@Caption("CurrentOddsTeam1")
	@Column(name = "CURRENT_ODDS_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentOddsTeam1() {
		return this.currentOddsTeam1;
	}

	public void setCurrentOddsTeam1(BigDecimal currentOddsTeam1) {
		this.currentOddsTeam1 = currentOddsTeam1;
	}

	@Caption("OpenSpreadTeam1")
	@Column(name = "OPEN_SPREAD_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenSpreadTeam1() {
		return this.openSpreadTeam1;
	}

	public void setOpenSpreadTeam1(BigDecimal openSpreadTeam1) {
		this.openSpreadTeam1 = openSpreadTeam1;
	}

	@Caption("OpenOddsTeam1")
	@Column(name = "OPEN_ODDS_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenOddsTeam1() {
		return this.openOddsTeam1;
	}

	public void setOpenOddsTeam1(BigDecimal openOddsTeam1) {
		this.openOddsTeam1 = openOddsTeam1;
	}

	@Caption("CurrentSpreadTeam2")
	@Column(name = "CURRENT_SPREAD_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentSpreadTeam2() {
		return this.currentSpreadTeam2;
	}

	public void setCurrentSpreadTeam2(BigDecimal currentSpreadTeam2) {
		this.currentSpreadTeam2 = currentSpreadTeam2;
	}

	@Caption("CurrentOddsTeam2")
	@Column(name = "CURRENT_ODDS_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentOddsTeam2() {
		return this.currentOddsTeam2;
	}

	public void setCurrentOddsTeam2(BigDecimal currentOddsTeam2) {
		this.currentOddsTeam2 = currentOddsTeam2;
	}

	@Caption("OpenSpreadTeam2")
	@Column(name = "OPEN_SPREAD_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenSpreadTeam2() {
		return this.openSpreadTeam2;
	}

	public void setOpenSpreadTeam2(BigDecimal openSpreadTeam2) {
		this.openSpreadTeam2 = openSpreadTeam2;
	}

	@Caption("OpenOddsTeam2")
	@Column(name = "OPEN_ODDS_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenOddsTeam2() {
		return this.openOddsTeam2;
	}

	public void setOpenOddsTeam2(BigDecimal openOddsTeam2) {
		this.openOddsTeam2 = openOddsTeam2;
	}

	@Caption("CurrentTotalLine")
	@Column(name = "CURRENT_TOTAL_LINE", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentTotalLine() {
		return this.currentTotalLine;
	}

	public void setCurrentTotalLine(BigDecimal currentTotalLine) {
		this.currentTotalLine = currentTotalLine;
	}

	@Caption("OpenTotalLine")
	@Column(name = "OPEN_TOTAL_LINE", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenTotalLine() {
		return this.openTotalLine;
	}

	public void setOpenTotalLine(BigDecimal openTotalLine) {
		this.openTotalLine = openTotalLine;
	}

	@Caption("CurrentOuOddsTeam1")
	@Column(name = "CURRENT_OU_ODDS_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentOuOddsTeam1() {
		return this.currentOuOddsTeam1;
	}

	public void setCurrentOuOddsTeam1(BigDecimal currentOuOddsTeam1) {
		this.currentOuOddsTeam1 = currentOuOddsTeam1;
	}

	@Caption("OpenOuOddsTeam1")
	@Column(name = "OPEN_OU_ODDS_TEAM1", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenOuOddsTeam1() {
		return this.openOuOddsTeam1;
	}

	public void setOpenOuOddsTeam1(BigDecimal openOuOddsTeam1) {
		this.openOuOddsTeam1 = openOuOddsTeam1;
	}

	@Caption("CurrentOuOddsTeam2")
	@Column(name = "CURRENT_OU_ODDS_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getCurrentOuOddsTeam2() {
		return this.currentOuOddsTeam2;
	}

	public void setCurrentOuOddsTeam2(BigDecimal currentOuOddsTeam2) {
		this.currentOuOddsTeam2 = currentOuOddsTeam2;
	}

	@Caption("OpenOuOddsTeam2")
	@Column(name = "OPEN_OU_ODDS_TEAM2", columnDefinition = "DECIMAL", precision = 6, scale = 3)
	public BigDecimal getOpenOuOddsTeam2() {
		return this.openOuOddsTeam2;
	}

	public void setOpenOuOddsTeam2(BigDecimal openOuOddsTeam2) {
		this.openOuOddsTeam2 = openOuOddsTeam2;
	}

	@Caption("ScoreTeam1")
	@Column(name = "SCORE_TEAM1", columnDefinition = "TINYINT UNSIGNED")
	public Short getScoreTeam1() {
		return this.scoreTeam1;
	}

	public void setScoreTeam1(Short scoreTeam1) {
		this.scoreTeam1 = scoreTeam1;
	}

	@Caption("ScoreTeam2")
	@Column(name = "SCORE_TEAM2", columnDefinition = "TINYINT UNSIGNED")
	public Short getScoreTeam2() {
		return this.scoreTeam2;
	}

	public void setScoreTeam2(Short scoreTeam2) {
		this.scoreTeam2 = scoreTeam2;
	}

	@Caption("CreatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false, columnDefinition = "DATETIME", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
