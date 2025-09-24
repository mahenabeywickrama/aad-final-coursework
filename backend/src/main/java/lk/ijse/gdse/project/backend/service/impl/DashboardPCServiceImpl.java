package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.dto.DashboardPCStatsDTO;
import lk.ijse.gdse.project.backend.repository.OrdersRepository;
import lk.ijse.gdse.project.backend.repository.PCRepository;
import lk.ijse.gdse.project.backend.service.DashboardPCService;
import org.springframework.stereotype.Service;

@Service
public class DashboardPCServiceImpl implements DashboardPCService {
    private final PCRepository pcRepository;
    private final OrdersRepository orderRepository;

    public DashboardPCServiceImpl(PCRepository pcRepository, OrdersRepository orderRepository) {
        this.pcRepository = pcRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public DashboardPCStatsDTO getDashboardPCStats() {
        long totalPCs = pcRepository.count();
        long totalSales = orderRepository.count();
        double totalRevenue = orderRepository.sumRevenue() != null ? orderRepository.sumRevenue() : 0.0;
        String topSeries = pcRepository.findTopSellingSeries();

        return new DashboardPCStatsDTO(totalPCs, totalSales, totalRevenue, topSeries);
    }
}
