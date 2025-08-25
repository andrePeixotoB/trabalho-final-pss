package br.ufes.dcomp.trabalhoProjetos.service;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;
import br.ufes.dcomp.trabalhoProjetos.model.Item;

public class CalculadoraService {

    // Fatores de emissão estáticos (kg CO2e por kg de material)
    // Para simplificar, estamos usando um fator médio para todos os itens.
    private static final double FATOR_EMISSAO_MEDIO = 7.0;

    /**
     * Calcula e atribui os valores de GWP e MCI a um objeto Item.
     * @param item O item para o qual os indicadores serão calculados.
     */
    public void calcularIndicadores(Item item) {
        // Cálculo de GWP (Global Warming Potential)
        double gwpBase = item.getMassaEstimadaKg() * FATOR_EMISSAO_MEDIO;
        // GWP evitado = GWP base - sobrecarga de reuso (5%)
        item.setGwpEvitado(gwpBase - (0.05 * gwpBase));

        // Cálculo de MCI (Material Circularity Indicator)
        // Q = 1 - soma das perdas de utilidade (defeitos)
        double somaPerdas = item.getDefeitos().stream()
                .mapToDouble(Defeito::getPercentualDesconto)
                .sum();

        // A soma dos descontos é limitada a 0.90, conforme a especificação
        if (somaPerdas > 0.90) {
            somaPerdas = 0.90;
        }

        double fatorQualidadeQ = 1.0 - somaPerdas;
        item.setMci(fatorQualidadeQ);
    }

    /**
     * Calcula o preço final de um item aplicando os descontos dos defeitos em cascata.
     * @param item O item cujo preço será calculado.
     * @return O preço final calculado.
     */
    public double calcularPrecoFinal(Item item) {
        double precoFinal = item.getPrecoBase();

        // Aplica os descontos em cascata para cada defeito associado
        for (Defeito defeito : item.getDefeitos()) {
            precoFinal = precoFinal * (1 - defeito.getPercentualDesconto());
        }

        // Garante que o preço final nunca seja menor que 5% do valor inicial
        double precoMinimo = item.getPrecoBase() * 0.05;
        if (precoFinal < precoMinimo) {
            return precoMinimo;
        }

        return precoFinal;
    }
}