package com.judicature.databackend.vo.semantic;

import java.util.List;

/**
 * @author Gabri
 */
public class ShortAnswerVO {

    private List<PolysemantSituationVO> polysemantSituationVOs;

    public List<PolysemantSituationVO> getPolysemantSituationVOs() {
        return polysemantSituationVOs;
    }

    public void setPolysemantSituationVOs(List<PolysemantSituationVO> polysemantSituationVOs) {
        this.polysemantSituationVOs = polysemantSituationVOs;
    }

    @Override
    public String toString() {
        return "ShortAnswerVO{" +
                "polysemantSituationVOs=" + polysemantSituationVOs +
                '}';
    }
}
