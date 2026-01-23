package cz.algone.common.enumAlias;

/** Interface podporující genericitu pro enums, zároveň vlastní metodu pro získávání konkrétní
 * hodnoty enum podle Stringu */
public interface IAlias {
    IAlias getAlias(String alias);
}
