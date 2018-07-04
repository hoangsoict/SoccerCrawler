
package com.soccercrawler.dal;

import com.xdev.dal.JPADAO;
import com.soccercrawler.entities.SoccermatchTemp;

/**
 * Home object for domain model class SoccermatchTemp.
 * 
 * @see SoccermatchTemp
 */
public class SoccermatchTempDAO extends JPADAO<SoccermatchTemp, Long> {
	public SoccermatchTempDAO() {
		super(SoccermatchTemp.class);
	}
}