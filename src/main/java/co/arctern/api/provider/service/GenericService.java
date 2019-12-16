package co.arctern.api.provider.service;

public interface GenericService {

    /**
     * fetch amount owed by user.
     *
     * @param userId
     * @return
     */
    public Double fetchUserOwedAmount(Long userId);

}
