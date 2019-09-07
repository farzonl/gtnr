%% Monte Carlo Solar Panel Power Production
clear
clc
close all
%{
    This Monte Calo simulation is to determine the power output of the solar panel . This monte carlo simulation utilizes
 sun angle data for May 4, 2012 at a latitude specified by the user generated from the 
U.S. Naval Observatory's Astronomical Applications Department. 
                                            < http://aa.usno.navy.mil/data/docs/AltAz.php >
The cloud factor is cimputed by producing

INPUTS:


OUTPUTS: 


References:
< http://ieeexplore.ieee.org.prx.library.gatech.edu/stamp/stamp.jsp?tp=&arnumber=564374 >
%}
%% Simulation Parameters
% INPUTS
nSamp = 7.5e4;  % No. of Samples
nBars = 35; % No. of bars to show on histograms

Latitude = 28.5;    % Latitude of the solar panel (determines the zenith angle @ noon)

SolarCellEffMin= 0.10;  % Solar Cell Conversion Minimum Efficiency
SolarCellEffMax = 0.20; % Solar Cell Conversion Minimum Efficiency

SolConstMin = 900;    % Minimum Solar Constant at sea-level [w/m^2]
SolConstMax = 1100; % Maximum Solar Constant at sea-level [w/m^2]

MinCF = 0.1;  % Corresponds to overcast sky conditions
MaxCF = 1.0;

% PLOTTING OPTIONS
DistributionPlots = 1;  % Plots the Random Variable Distribution Plots
PowerPlots = 1; % Plots the Power Output plots
ProbabilityPlots = 1;   % Plot the Power Output Probability plots

%% Physical Constants

if (Latitude > 23.4)
    MaxAngle = deg2rad(90-Latitude + 23.4);  % Maximum angle of the sun relative to the horizon [rad]
else
    MaxAngle = pi/2;    % Maxiimum angle of the sun relative to the horizon [rad]
end

%% Monte Carlo Simulation Calculation
% Distributions of unkown variables
SolConst = SolConstMin + (SolConstMax - SolConstMin)*rand(nSamp,1); % Solar Constant Distribution
SolCellEff = SolarCellEffMin + (SolarCellEffMax - SolarCellEffMin)*rand(nSamp,1); % Solar Cell Efficency Distribution
theta = rand(nSamp,1)*(MaxAngle); % Unifrom distribution of sun incidence angles
CloudFactor = MinCF + (MaxCF-MinCF).*rand(nSamp,1);  % Uniform distribution of "Cloud Factor" < IEEE Paper, Table I >

PowerOut = SolConst.*CloudFactor.*SolCellEff.*sin(theta);

%% Plotting
thetad = rad2deg(theta);   % Angle distribution in deg [º]
bins = linspace(0,max(SolConst)*max(SolCellEff),nSamp)';

% Random Variable Distribution Plots
if (DistributionPlots)
    nBar2 = floor(0.5*nBars); % No. of bars for the Distribution Histograms
    
    figure(1)
    subplot(2,2,1)
    plot(thetad,'k.','MarkerSize',0.5)
    title('Sun Incidence Angle Distribution','fontsize',12)
    ylabel('Degrees (º)','fontsize',12)

    subplot(2,2,2)
    plot(CloudFactor,'k.','MarkerSize',0.5)
    title('Cloud Factor Distribution','fontsize',12)
    ylabel('Cloud Factor','fontsize',12)

    subplot(2,2,3)
    plot(SolConst,'k.','MarkerSize',0.5)
    title('Solar Constant Distribution','fontsize',12)
    ylabel('Solar Constant (Wm^-^2)','fontsize',12)
    
    subplot(2,2,4)
    plot(SolCellEff,'k.','MarkerSize',0.5)
    title('Solar Cell Efficency Distribution','fontsize',12)
    ylabel('Solar Cell Efficency','fontsize',12)
    
    figure(2)
    subplot(2,2,1)
    hist(thetad,nBar2);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w')
    title('Sun Incidence Angle Distribution','fontsize',12)
    xlabel('Degrees (º)','fontsize',12)
    ylabel('Frequency','fontsize',12)

    subplot(2,2,2)
    hist(CloudFactor,nBar2);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w')
    title('Cloud Factor Distribution','fontsize',12)
    xlabel('Cloud Factor','fontsize',12)
    ylabel('Frequency','fontsize',12)

    subplot(2,2,3)
    hist(SolConst,nBar2);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w')
    title('Solar Constant Distribution','fontsize',12)
    xlabel('Solar Constant (Wm^-^2)','fontsize',12)
    ylabel('Frequency','fontsize',12)

    subplot(2,2,4)
    hist(SolCellEff,nBar2);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w')
    title('Solar Cell Efficency Distribution','fontsize',12)
    xlabel('Solar Cell Efficency','fontsize',12)
    ylabel('Frequency','fontsize',12)
  
end


% Power Output Plots
if (PowerPlots)
    figure(3)
    subplot(1,2,1)
    plot(thetad,PowerOut,'k.','MarkerSize',0.5)
    title({'Power Output PDF';[num2str(nSamp) ' Cases']},'fontsize',12);
    xlabel('Degrees (º)','fontsize',12)
    ylabel('Power (W)','fontsize',12)

    subplot(1,2,2)
    hist(PowerOut,nBars);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w')
    title('Power Output Distribution','fontsize',12)
    xlabel('Power (W)','fontsize',12)
    ylabel('Frequency','fontsize',12)
end

% Power PDF and CDF
if (ProbabilityPlots)
    figure(4);
    subplot(1,2,1);
    hist(PowerOut,nBars);
    h  = findobj(gca,'Type','patch');
    set(h,'FaceColor','k','EdgeColor','w');
    axes = axis(gca);
    axis([axes(1) (bins(end)+25) axes(3:4)])  
    title({'Power Output PDF';[num2str(nSamp) ' Cases']},'fontsize',12);
    xlabel('Power (W)','fontsize',12);
    ylabel('Frequency','fontsize',12);

    subplot(1,2,2)
    POcdf = cumsum(histc(PowerOut,bins))*(100/nSamp);
    plot(bins,POcdf(end:-1:1),'k','LineWidth',1.5);
    axis([0,bins(end)+25,0,100]);
    title({'Power Output CDF';[num2str(nSamp) ' Cases']},'fontsize',12);
    xlabel('Power (W)','fontsize',12);
    ylabel('Probability (%)','fontsize',12);
end